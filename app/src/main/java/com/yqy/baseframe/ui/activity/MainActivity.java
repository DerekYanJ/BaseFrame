package com.yqy.baseframe.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import com.yqy.baseframe.R;
import com.yqy.baseframe.frame.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

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
                if(linearlayout.getVisibility() == View.VISIBLE){
                    //当前状态为显示  需要将其隐藏
                    // get the center for the clipping circle
                    int cx= 0;
                    int cy = 0;

                    // get the initial radius for the clipping circle
                    int initialRadius = linearlayout.getWidth()*2;

                    // create the animation (the final radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(linearlayout, cx, cy, initialRadius, 0);

                    // make the view invisible when the animation is done
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            linearlayout.setVisibility(View.INVISIBLE);
                        }
                    });

                    // start the animation
                    anim.start();
                }else{
                    //当前状态为隐藏  需要将其显示
//                    // get the center for the clipping circle
                    int cx= 0;
                    int cy = 0;

                    // get the final radius for the clipping circle
                    int finalRadius = Math.max(linearlayout.getWidth(), linearlayout.getHeight())*2;

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(linearlayout, cx, cy, 0, finalRadius);

                    // make the view visible and start the animation
                    linearlayout.setVisibility(View.VISIBLE);
                    anim.start();
                }
                break;
        }
    }
}
