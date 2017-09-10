package com.yqy.frame.http;

import android.content.Context;

import com.yqy.frame.utils.JsonUtil;
import com.yqy.frame.utils.L;

import java.util.Map;

import rx.Subscriber;

/**
 * @author derekyan
 * @desc
 * @date 2016/12/6
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberResultListener mSubscriberResultListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context mContext;
    private int requestId; //请求ID 用于识别接口

    private boolean cancelable = false;//ProgressDialog是否可以关闭
    private boolean isShowDialog = true;//是否显示ProgressDialog

    public int getRequestId() {
        return requestId;
    }

    public ProgressSubscriber(SubscriberResultListener SubscriberResultListener, Context context, int requestId) {
        mSubscriberResultListener = SubscriberResultListener;
        mContext = context;
        this.requestId = requestId;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable);
    }

    public ProgressSubscriber(SubscriberResultListener SubscriberResultListener, Context context, int requestId, boolean flag) {
        mSubscriberResultListener = SubscriberResultListener;
        mContext = context;
        this.requestId = requestId;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, flag);
    }

    public ProgressSubscriber(SubscriberResultListener SubscriberResultListener, Context context, int requestId, String message) {
        mSubscriberResultListener = SubscriberResultListener;
        mContext = context;
        this.requestId = requestId;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable, message);
    }


    public ProgressSubscriber(SubscriberResultListener SubscriberResultListener, Context context, int requestId, String message,boolean showDialog) {
        mSubscriberResultListener = SubscriberResultListener;
        mContext = context;
        this.requestId = requestId;
        isShowDialog = showDialog;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable, message);
    }

    /**
     * @param SubscriberResultListener
     * @param context
     * @param requestId                接口标识
     * @param flag                     是否允许关闭progressDialog
     * @param message                  progressDialog中message内容
     */
    public ProgressSubscriber(SubscriberResultListener SubscriberResultListener, Context context, int requestId, boolean flag, String message) {
        mSubscriberResultListener = SubscriberResultListener;
        mContext = context;
        this.requestId = requestId;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, flag, message);
    }

    public ProgressSubscriber<T> setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
        return this;
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 请求网络开始前
     */
    @Override
    public void onStart() {
        if(isShowDialog)
            showProgressDialog();
    }

    /**
     * 请求网络结束后
     */
    @Override
    public void onCompleted() {
        if(isShowDialog)
            dismissProgressDialog();
    }

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法
     *
     * @param e 异常信息
     */
    @Override
    public void onError(Throwable e) {
        if(isShowDialog)
            dismissProgressDialog();
        if(e.getMessage().indexOf("errorCode") != -1){
            Map<String,String> errorMap = JsonUtil.jsonToMap1(e.getMessage());
            if(Integer.parseInt(errorMap.get("errorCode")) == 1001){
                //重新登陆
//                mContext.startActivity(new Intent(mContext, LoginActivity.class));
//                ((AbstractActivity) mContext).finish();
            }else if (mSubscriberResultListener != null)
                mSubscriberResultListener.onError(Integer.parseInt(errorMap.get("errorCode")),errorMap.get("errorMsg"),requestId);
        }else {
            mSubscriberResultListener.onError( -1 ,e.getMessage(),requestId);
        }
        if(L.isShow) {
            e.printStackTrace();
            L.e("error",e.getMessage());
        }
    }

    /**
     * 对返回数据进行操作的回调， UI线程
     *
     * @param t 返回数据bean
     */
    @Override
    public void onNext(T t) {
        try{
            if (mSubscriberResultListener != null)
                mSubscriberResultListener.onNext(t, requestId);
        }catch (Exception e){
            if(L.isShow)
                e.printStackTrace();
        }
    }

    /**
     * 当cancel掉ProgressDialog的时候，能够取消订阅，也就取消了当前的Http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    public void onCancelRequest(){
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
