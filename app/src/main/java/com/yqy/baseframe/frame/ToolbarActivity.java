package com.yqy.baseframe.frame;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yqy.baseframe.R;
import com.yqy.baseframe.utils.ToolBarHelper;

import butterknife.ButterKnife;

/**
 * @author derekyan
 * @desc 有状态栏
 * @date 2016/12/6
 */

public abstract class ToolbarActivity  extends AbstractActivity implements View.OnClickListener {

    public Toolbar getActionBarToolbar() {
        return mToolbar;
    }

    protected Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithActionBar(preView());
        mContext = this;
        unbinder = ButterKnife.bind(this);
        initView();
        addListener();
        initData();
        mOnClickBackListener = getOnBackClickListener();
    }

    /**
     * setContentViewWithActionBar()
     *
     * @param layoutResID
     */
    protected void setContentViewWithActionBar(int layoutResID) {

        ToolBarHelper mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolbar = mToolBarHelper.getToolBar();
        setContentView(mToolBarHelper.getContentView()); /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(mToolbar); /*自定义的一些操作*/
        onCreateCustomToolBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickBackListener != null) {
                    mOnClickBackListener.onClickBackLister();
                } else {
                    finish();
                }
            }
        });

    }

    /**
     * 设置标题
     * @param title
     */
    protected void setToolBarCenterTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
        if (mToolbar != null) {
            View view = mToolbar.findViewById(R.id.center_title);
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.toolbar_center_title, mToolbar, true).findViewById(R.id.center_title);
            }
            ((TextView) view).setText(title);
        }

    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
