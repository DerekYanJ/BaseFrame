package com.yqy.baseframe;

import android.view.View;
import android.widget.LinearLayout;

import com.yqy.baseframe.frame.ToolbarActivity;

import butterknife.BindView;

/**
 * @author derekyan
 * @desc
 * @date 2016/12/24
 */

public class Test1 extends ToolbarActivity {
    @BindView(R.id.activity_main)
    LinearLayout mainLl;

    @Override
    protected int preView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolBarCenterTitle("测试");

    }

    @Override
    protected void addListener() {
        mainLl.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected OnClickBackListener getOnBackClickListener() {
        return new OnClickBackListener() {
            @Override
            public void onClickBackLister() {
                showSnackbar("别退出");
            }
        };
    }

    @Override
    public void onClick(View view) {

    }
}
