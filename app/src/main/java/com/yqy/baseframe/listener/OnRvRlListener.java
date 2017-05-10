package com.yqy.baseframe.listener;

/**
 * OnRecyclerViewRelativeLayoutListener
 * Created by DerekYan on 2017/5/10.
 */

public interface OnRvRlListener {
    void onRefresh(); //下拉刷新
    void onLoadMore();//加载更多
    void onTop();//置顶
}
