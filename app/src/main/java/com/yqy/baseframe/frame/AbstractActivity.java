package com.yqy.baseframe.frame;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yqy.baseframe.R;
import com.yqy.baseframe.utils.glide.GlideCircleTransform;

import butterknife.Unbinder;

/**
 * @author derekyan
 * @desc activity基类
 * @date 2016/12/6
 */

public abstract class AbstractActivity extends AppCompatActivity  implements View.OnClickListener{
    private ProgressDialog mProgressDialog;
    public Unbinder unbinder; //butterKnife 对象
    public Context mContext;


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
     * Toast 吐丝
     * @param tip 提示文字
     */
    public void showToast(String tip){
        Toast.makeText(this, tip , Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示加载动画
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
            mProgressDialog.setMessage(getString(R.string.progress_dialog_str1));
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
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //拦截系统返回键单击事件
            mOnClickBackListener = getOnBackClickListener();
            if(mOnClickBackListener != null){
                //触发我们自己的返回键监听事件
                mOnClickBackListener.onClickBackLister();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public OnClickBackListener mOnClickBackListener;
    public interface OnClickBackListener{
        void onClickBackLister();
    }

    public void setOnClickBackListener(OnClickBackListener mOnClickBackListener){
        this.mOnClickBackListener = mOnClickBackListener;
    }

    /**
     * 处理返回数据
     * @param data
     * @param id
     * @param <T>
     */
    public <T> void doData(T data, int id){}
    public <T> void doData(T data, int id, String qid){}

    /*
	 * 倒计时 秒数
	 */
    public int sendMsmCount = 60;

    /**
     * 倒计时
     * @param tv
     * @param sendMsmCount 秒数
     */
    public void showTimeDown(final TextView tv, int sendMsmCount) {
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
