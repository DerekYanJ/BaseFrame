package com.yqy.frame.frame;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yqy.frame.R;
import com.yqy.frame.utils.ToolBarHelper;

/**
 * @author derekyan
 * @desc 有状态栏
 * @date 2016/12/6
 */

public abstract class BaseActivity extends AbstractActivity implements View.OnClickListener {

    public Toolbar getActionBarToolbar() {
        return mToolbar;
    }

    protected Toolbar mToolbar;

    /**
     * 设置是否展示Toolbar 默认为显示
     */
    public boolean setIsShowToolbar(){
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(setIsShowToolbar())
            setContentViewWithActionBar(preView());
        else
            setContentView(preView());
        mContext = this;
//        unbinder = ButterKnife.bind(this);
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
        try {
            ToolBarHelper mToolBarHelper = new ToolBarHelper(this, layoutResID);
            mToolbar = mToolBarHelper.getToolBar();
            setContentView(mToolBarHelper.getContentView()); /*把 toolbar 设置到Activity 中*/
            setSupportActionBar(mToolbar); /*自定义的一些操作*/
            onCreateCustomToolBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickBackListener != null) {
                        mOnClickBackListener.onClickBack();
                    } else {
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题
     * @param title
     */
    protected void setToolBarCenterTitle(String title) {
        if(!setIsShowToolbar()) return;
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

    /**
     * 设置toolbar返回键
     * @param resId
     */
    protected void setToolbarBackIcon(int resId){
        try {
            getActionBarToolbar().setNavigationIcon(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideToolbar(){
        getActionBarToolbar().setVisibility(View.GONE);
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

    @Override
    protected OnClickBackListener getOnBackClickListener() {
        return null;
    }
}
