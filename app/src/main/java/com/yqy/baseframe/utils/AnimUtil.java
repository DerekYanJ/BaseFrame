package com.yqy.baseframe.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.compat.BuildConfig;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * 动画
 * Created by DerekYan on 2017/5/8.
 */

public class AnimUtil {

    /**
     * 设置View的显示隐藏动画
     * @param view
     * @param visibility
     */
    public static void setVisibility(final View view, int visibility){
        if( BuildConfig.VERSION_CODE > 21 ){
            if(visibility == View.VISIBLE){
                //当前状态为显示  需要将其隐藏
                // get the center for the clipping circle
                int cx= 0;
                int cy = 0;

                // get the initial radius for the clipping circle
                int initialRadius = view.getWidth()*2;

                // create the animation (the final radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

                // make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                    }
                });

                // start the animation
                anim.start();
            }else{
                //当前状态为隐藏  需要将其显示
                // get the center for the clipping circle
                int cx= 0;
                int cy = 0;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(view.getWidth(), view.getHeight())*2;

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                view.setVisibility(View.VISIBLE);
                anim.start();
            }
        }else{
            view.setVisibility(visibility);
        }
    }
}
