package com.yqy.baseframe.ui.activity;

import android.content.Intent;
import android.view.View;

import com.yqy.baseframe.R;
import com.yqy.baseframe.frame.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int preView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolBarCenterTitle("我是首页");

    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected OnClickBackListener getOnBackClickListener() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                startActivity(new Intent(this,RecyclerViewActivity.class));
                break;
        }
    }
}
