package com.yqy.baseframe.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.yqy.baseframe.R;
import com.yqy.baseframe.listener.OnRvRlListener;


/**
 * 组合控件：recyclerview上下拉刷新和置顶
 * Created by DerekYan on 2017/5/10.
 */

public class RecyclerViewRelativeLayout extends RelativeLayout {
    private SwipeRefreshLayout swiperefreshlayout;
    private RecyclerView recyclerview;
    private FloatingActionButton floatingactionbutton;
    private OnRvRlListener listener;
    private LinearLayoutManager layoutmanager;
    public int pageNum = 20; //每页显示条目数量
    public boolean isLoadMore = true; //是否可以加载更多
    public int currentItemNum = 0;//当前条目数量

    public RecyclerViewRelativeLayout(Context context) {
        super(context);
        initView(context);
    }

    public RecyclerViewRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecyclerViewRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化View
     * @param context
     */
    private void initView(Context context){
        View.inflate(context, R.layout.layout_recyclerview_relativelayout,this);

        swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swiperefreshlayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent));
        swiperefreshlayout.setSize(SwipeRefreshLayout.LARGE);
        swiperefreshlayout.setProgressViewEndTarget(true, 100);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        layoutmanager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(layoutmanager);

        floatingactionbutton = (FloatingActionButton) findViewById(R.id.floatingactionbutton);
    }

    /**
     * 设置事件监听
     * @param listener
     */
    public void setListener(OnRvRlListener listener) {
        this.listener = listener;
        addListener();
    }

    /**
     * 设置刷新状态
     * @param flag
     */
    public void setRefreshing(final boolean flag){
        swiperefreshlayout.post(new Runnable() {
            @Override
            public void run() {
                swiperefreshlayout.setRefreshing(flag);
            }
        });
    }

    /**
     * 设置更新的item的数量
     * 数据刷新后必须调用 用于确定是否可以加载更多和确定是不是要显示置顶fab
     * @param size
     */
    public void setUpdateSize( int size ){
        if(size < pageNum || size == 0){
            isLoadMore = false;
        }
        if(currentItemNum == 0 && isLoadMore){
            currentItemNum = size;
        }
    }

    private void addListener(){
        //下拉刷新
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(listener != null) {
                    isLoadMore = true;
                    currentItemNum = 0;
                    listener.onRefresh();
                }
            }
        });
        //上拉加载更多
        recyclerview.addOnScrollListener(
                new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        //是否能向上滚动，false表示已经滚动到底部
                        if(!recyclerView.canScrollVertically(1)){
                            if(!swiperefreshlayout.isRefreshing()){
                                if(listener != null && isLoadMore)
                                    listener.onLoadMore();
                            }
                        }
                        if(layoutmanager.findLastVisibleItemPosition() > (pageNum - 1)){
                            if(!floatingactionbutton.isShown())
                                floatingactionbutton.show();
                        }else{
                            if(floatingactionbutton.isShown())
                                floatingactionbutton.hide();
                        }
                    }
                }
        );
        //置顶
        floatingactionbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //滚动到第一个item
                recyclerview.smoothScrollToPosition(0);
            }
        });
    }

}
