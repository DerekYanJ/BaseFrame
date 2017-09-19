package com.yqy.testframe;

import android.view.View;

import com.yqy.frame.http.ProgressSubscriber;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends MBaseActivity {

    @Override
    protected int preView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolBarCenterTitle("我的");
    }

    @Override
    protected void addListener() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqLogin("username","pwd");
            }
        });
    }

    /**
     * 登录
     * @param phone 手机号
     * @param pwd   密码
     */
    public void reqLogin(String phone, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("pwd", pwd);
        HttpRequest.getInstance().getLogin(new ProgressSubscriber<Login>(this, this,0, "登陆中", false), params);
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

    }
}
