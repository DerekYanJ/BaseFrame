package com.yqy.baseframe.frame;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yqy.baseframe.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author derekyan
 * @desc fragment基类
 * @date 2016/12/26
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public View view;
    private ProgressDialog mProgressDialog;
    public Unbinder unbinder; //butterknife 对象
    public Context mContext;

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
        unbinder.unbind();
        super.onDestroy();
    }

    public <T> void doData(T data, int id){}
    public <T> void doData(T data, int id, String qid){}
}
