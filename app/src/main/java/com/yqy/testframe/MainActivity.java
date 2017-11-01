package com.yqy.testframe;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yqy.frame.http.ProgressSubscriber;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends MBaseActivity {

    private LinearLayout templinearlayout;

    private LinearLayout linearlayout;
    private LinearLayout info_linearlayout;
    private ImageView imageview;
    MyScrollView scrollview;

    private float density = -1f; //屏幕密度
    private int oldY = -1;

    @Override
    public boolean setIsShowToolbar() {
        return true;
    }

    @Override
    protected int preView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolBarCenterTitle("我的");

        /*templinearlayout = (LinearLayout) findViewById(R.id.templinearlayout);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        info_linearlayout = (LinearLayout) findViewById(R.id.info_linearlayout);

        imageview = (ImageView) findViewById(R.id.imageview);
        scrollview = (MyScrollView) findViewById(R.id.scrollview);*/
    }

    @Override
    protected void addListener() {
        /*scrollview.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if(density == -1) {
                    density = findViewById(R.id.templinearlayout).getLayoutParams().height/200f;
                }
                Log.e("yqy_y",scrollY + "--" + density);
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = scrollY;
                mHandler.sendMessage(msg);
            }
        });*/
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                int y = (int) msg.obj;
                oldY = y;
                if(y >= 100 * density && templinearlayout.getVisibility() == View.VISIBLE){
                    templinearlayout.setVisibility(View.GONE);
                    linearlayout.setVisibility(View.VISIBLE);
                }
                if(y < 100 * density && linearlayout.getVisibility() == View.VISIBLE){
                    linearlayout.setVisibility(View.GONE);
                    templinearlayout.setVisibility(View.VISIBLE);
                }

                if(y < 100 && templinearlayout.getVisibility() == View.VISIBLE){
                    info_linearlayout.setTranslationY( oldY - y );
                }
            }
        }
    };

    /**
     * 登录
     * @param phone 手机号
     * @param pwd   密码
     */
    public void reqLogin(String phone, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("pwd", pwd);
        HttpRequest.getInstance().getLogin(new ProgressSubscriber<Login>(this, this,"login", "登陆中", false), params);
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
