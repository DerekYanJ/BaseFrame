package com.yqy.frame.frame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yqy.frame.R;
import com.yqy.frame.http.ProgressSubscriber;
import com.yqy.frame.http.SubscriberResultListener;
import com.yqy.frame.listener.OnAlertDialogListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * @author derekyan
 * @desc fragment基类
 * @date 2016/12/26
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener,SubscriberResultListener {
    public View view;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mAlertDialog;
    public Unbinder unbinder; //butterknife 对象
    public Context mContext;

    /** 请求对象的集合 **/
    private Map<String, Subscriber> mSubscriberMap = new HashMap<>();

    /**
     * 请求集合
     * @param subscriber
     */
    public void addSubscriber(ProgressSubscriber subscriber){
        try {
            if(mSubscriberMap == null) return;
            String requestId = subscriber.getRequestId(); //请求id
            if(mSubscriberMap.containsKey(requestId)){
                if(mSubscriberMap.get(requestId).isUnsubscribed())
                    //如果没有取消订阅 则取消订阅
                    mSubscriberMap.get(requestId).unsubscribe();
            }
            //添加到集合
            mSubscriberMap.put(requestId,subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空subscriber
     */
    public void clearSubscriber(){
        try {
            if(mSubscriberMap == null) return;
            Set<String> mSet = mSubscriberMap.keySet();
            for (String requestId : mSet) {
                Subscriber subscriber = mSubscriberMap.get(requestId);
                if( subscriber != null && subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe(); //取消订阅
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSubscriberMap.clear(); //清空
    }

    /**
     * 请求结束后移除map存的对象
     * @param requestId
     */
    public void removeSubscriber(String requestId){
        try {
            if(mSubscriberMap == null) return;
            if(mSubscriberMap.containsKey(requestId)){
                Subscriber subscriber = mSubscriberMap.get(requestId);
                if( subscriber != null && subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe(); //取消订阅
                }
                mSubscriberMap.remove(requestId);//移除
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(preView(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mContext = this.getActivity();
        initView();
        addListener();
        initData();
    }

    /**
     * 预备布局contenView id
     */
    protected abstract int preView();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 添加监听事件
     */
    protected abstract void addListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置刷新状态
     * @param mSwipeRefreshLayout
     * @param flag
     */
    public void setRefreshing(final SwipeRefreshLayout mSwipeRefreshLayout, final boolean flag){
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(flag);
            }
        });
    }

    public <T> void doData(T data, String id){}
    public <T> void doData(T data, String id, String qid){}

    @Override
    public void onNext(Object o, String requestId) {
        removeSubscriber(requestId);
        doData(o,requestId);
    }

    /**
     * 处理错误信息
     * @param errorCode
     * @param msg
     */
    @Override
    public void onError(int errorCode, String msg,String requestId) {
        removeSubscriber(requestId);
        if(getActivity() != null)
            ((AbstractActivity)getActivity()).onError(errorCode,msg,requestId);
    }

    /**
     * 加载网络图片并显示到ImageView  (圆形)
     * @param url 图片地址
     * @param mImageView 要显示的ImageView
     */
    public void loadCircleImg(String url, ImageView mImageView){
        ((AbstractActivity) mContext).loadCircleImg(url,mImageView);
    }

    /**
     * 加载网络图片并显示到ImageView
     * @param url 图片地址
     * @param mImageView 要显示的ImageView
     */
    public void loadImg(String url, ImageView mImageView){
        ((AbstractActivity) mContext).loadImg(url,mImageView);
    }

    /**
     * 根布局下方提示的类似Toast 用户可滑动删除
     * @param tip 提示文字
     */
    protected void showSnackbar(String tip){
        ((AbstractActivity)mContext).showSnackbar(tip);
    }

    /**
     * 吐丝
     * @param tip 提示文字
     */
    protected void showToast(String tip){
        Toast.makeText(mContext, tip , Toast.LENGTH_SHORT).show();
    }

    /**
     * 展示对话弹框
     * @param messageStr
     * @param cancelStr
     * @param rightStr
     * @param listener
     */
    public void showAlertDialog(String messageStr, String cancelStr, String rightStr,
                                final OnAlertDialogListener listener){
        if(mAlertDialog == null){
            mAlertDialog = new AlertDialog.Builder(mContext);
        }
        mAlertDialog.setMessage(messageStr)
                .setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(listener != null) listener.onNegative();
                    }
                })
                .setPositiveButton(rightStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(listener != null) listener.onPositive();
                    }
                });
        mAlertDialog.show();
    }

    /**
     * 显示加载动画
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
        }
        if (TextUtils.isEmpty(message))
            mProgressDialog.setMessage(getString(R.string.str_progress_msg_load));
        else
            mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * 取消加载动画
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        dismissProgressDialog();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //取消订阅
        if(unbinder != null) unbinder.unbind();
        //清空清空subscriber
        clearSubscriber();
        super.onDestroy();
    }

}
