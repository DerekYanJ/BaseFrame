package com.yqy.baseframe;

import android.view.View;

import com.yqy.baseframe.frame.ToolbarActivity;
import com.yqy.baseframe.http.HttpRequest;
import com.yqy.baseframe.http.ProgressSubscriber;
import com.yqy.baseframe.http.SubscriberResultListener;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * @author derekyan
 * @desc 测试类
 * @date 2016/12/6
 */

public class Test extends ToolbarActivity{


    @Override
    protected int preView() {
        return 0;
    }

    @Override
    protected void initView() {
        //此页面标题
        setToolBarCenterTitle("测试");

    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void initData() {
        //TODO SOMETHING
        req();
    }

    @Override
    protected OnClickBackListener getOnBackClickListener() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    /**
     * 请求 处理返回数据
     */
    private void req(){
        Map<String,String> params = new HashMap<>();
        params.put("service","");
        //带progress
        HttpRequest.getInstance().getResult(new ProgressSubscriber<Object>(new SubscriberResultListener() {
            @Override
            public void onNext(Object o, int requestId) {
                doData(o,requestId);
//                doData(o,requestId,"");
            }

            @Override
            public void onError(int errorCode, String msg) {

            }
        },this,0),params);

        //不带progress
        HttpRequest.getInstance().getResult(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        },params);
    }

    @Override
    public <T> void doData(T data, int id) {
        super.doData(data, id);
        //TODO 在这里处理数据
    }

    @Override
    public <T> void doData(T data, int id, String qid) {
        super.doData(data, id, qid);
        //TODO 或者在这里 视情况
    }
}
