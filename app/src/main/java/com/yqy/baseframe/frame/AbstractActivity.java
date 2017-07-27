package com.yqy.baseframe.frame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yqy.baseframe.R;
import com.yqy.baseframe.http.ProgressSubscriber;
import com.yqy.baseframe.http.SubscriberResultListener;
import com.yqy.baseframe.listener.OnAlertDialogListener;
import com.yqy.baseframe.utils.glide.GlideCircleTransform;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.Unbinder;
import rx.Subscriber;

/**
 * @author derekyan
 * @desc activity基类
 * @date 2016/12/6
 */

public abstract class AbstractActivity extends AppCompatActivity implements View.OnClickListener,SubscriberResultListener {
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mAlertDialog;
    public Unbinder unbinder; //butterKnife 对象
    public Context mContext;
    public int pageNum = 20; //每页显示条目数量
    public boolean isLoadMore = true;//是否可以加载更多

    /** 请求对象的集合 **/
    private Map<Integer, Subscriber> mSubscriberMap = new HashMap<>();

    /**
     * 请求集合
     * @param subscriber
     */
    public void addSubscriber(ProgressSubscriber subscriber){
        try {
            if(mSubscriberMap == null) return;
            int requestId = subscriber.getRequestId(); //请求id
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
            Set<Integer> mSet = mSubscriberMap.keySet();
            for (int requestId : mSet) {
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
    public void removeSubscriber(int requestId){
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

    /** 预备布局contenView id */
    protected abstract int preView();

    /** 初始化view */
    protected abstract void initView();

    /** 添加监听事件 */
    protected abstract void addListener();

    /** 初始化数据 */
    protected abstract void initData();

    /** 返回键(标题栏、虚拟键) 点击事件*/
    protected abstract OnClickBackListener getOnBackClickListener();

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

    /**
     * 处理返回数据
     * @param data
     * @param id
     * @param <T>
     */
    public <T> void doData(T data, int id){}
    public <T> void doData(T data, int id, String qid){}

    @Override
    public void onNext(Object o, int requestId) {
        removeSubscriber(requestId);
        doData(o,requestId);
    }

    /**
     * 处理错误信息
     * @param errorCode
     * @param msg
     */
    @Override
    public void onError(int errorCode, String msg,int requestId) {
        removeSubscriber(requestId);
        //这里可以根据errorCode或者msg做一些全局的处理
        if(msg.indexOf("session") != -1 || requestId == 1001){
            //登录信息超时
//            Intent intent = new Intent(this,LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
        }
        else if(msg.indexOf("http.HttpRequest") != -1) {
            showToast("请求超时");
        }else if(msg.indexOf("HTTP 500 Internal Server Error") != -1){
            showToast("请求异常");
        }else  showToast(msg);
    }

    /**
     * 不带progress的请求
     * @param params
     */
    public void reqNoProgress(Map<String,String> params){
        /*HttpRequest.getInstance().getResult(new Subscriber<JSONObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JSONObject o) {

            }
        },params);*/
    }

    /**
     * 说明：隐藏软键盘
     *
     * @param editText
     *            当前输入框
     * @author liuwei
     * @version 1.0
     * @since 2014-10-16 下午07:35:33
     */
    public void hideSoftInput(final EditText editText) {
        try {
            hideSoftInput(editText.getWindowToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 说明：隐藏软键盘
     *
     * @param binder
     *            当前窗口所对应的IBinder对象
     * @author liuwei
     * @version 1.0
     * @since 2014-10-16 下午07:35:33
     */
    public void hideSoftInput(final IBinder binder) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(binder, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载网络图片并显示到ImageView  (圆形)
     * @param url 图片地址
     * @param mImageView 要显示的ImageView
     */
    public void loadCircleImg(String url, ImageView mImageView){
        Glide.with(this).load(url)
                .placeholder(R.mipmap.ic_launcher) //加载前图片
                .error(R.mipmap.ic_launcher)  //加载失败图片
                .transform(new GlideCircleTransform(this))  //切圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL)  //缓存 全尺寸和适应缓存
                .into(mImageView);  //要显示的控件
    }

    /**
     * 加载网络图片并显示到ImageView
     * @param url 图片地址
     * @param mImageView 要显示的ImageView
     */
    public void loadImg(String url, ImageView mImageView){
        Glide.with(this).load(url)
                .placeholder(R.mipmap.ic_launcher) //加载前图片
                .error(R.mipmap.ic_launcher)  //加载失败图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)  //缓存 全尺寸和适应缓存
                .into(mImageView);  //要显示的控件
    }

    /**
     * Snackbar 根布局下方提示的类似Toast 用户可滑动删除
     * @param tip 提示文字
     */
    public void showSnackbar(String tip){
        Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), tip, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Toast
     * @param tip 提示文字
     */
    public void showToast(String tip){
        Toast.makeText(this, tip , Toast.LENGTH_SHORT).show();
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
            mAlertDialog = new AlertDialog.Builder(this);
        }
        mAlertDialog.setMessage(messageStr)
                .setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        listener.onNegative();
                    }
                })
                .setPositiveButton(rightStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        listener.onPositive();
                    }
                });
        mAlertDialog.show();
    }

    /**
     * 显示加载弹窗
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
        }
        if(TextUtils.isEmpty(message))
            mProgressDialog.setMessage(getString(R.string.str_progress_msg_load));
        else
            mProgressDialog.setMessage(message);
        if(!mProgressDialog.isShowing()){
            mProgressDialog.show();
        }
    }

    /**
     * 取消加载动画
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (!isFinishing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //butterknife取消绑定
        if(unbinder != null) unbinder.unbind();
        //清空清空subscriber
        clearSubscriber();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //拦截系统返回键单击事件
            mOnClickBackListener = getOnBackClickListener();
            if(mOnClickBackListener != null){
                //触发我们自己的返回键监听事件
                mOnClickBackListener.onClickBack();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public OnClickBackListener mOnClickBackListener;
    public interface OnClickBackListener{
        void onClickBack();
    }

    public void setOnClickBackListener(OnClickBackListener mOnClickBackListener){
        this.mOnClickBackListener = mOnClickBackListener;
    }

    /*
	 * 倒计时 秒数
	 */
    public int sendMsmCount = 60;

    /**
     * 倒计时
     * @param tv
     * @param sendMsmCount 秒数
     */
    public void startTimeDown(final TextView tv, int sendMsmCount) {
        tv.setEnabled(false);
        tv.setTag(sendMsmCount);
        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                int tag = (Integer) tv.getTag();
                tag--;
                if (tag >= 0) {
                    tv.setText(tag + "s");
                    tv.setTag(tag);
                    tv.postDelayed(this, 1000);
                } else if (tag >= -1) {
                    tv.setText("获取验证码");
                    tv.setEnabled(true);
                } else {
                    tv.setText("获取验证码");
                }
            }
        }, 1000);
    }
}
