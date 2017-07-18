package com.yqy.baseframe.frame;

import android.view.View;

import com.yqy.baseframe.bean.Result;
import com.yqy.baseframe.listener.OnRecyclerViewListener;

import java.util.List;

/**
 *
 * Created by yanqy on 2017/3/29.
 */

public class MyRecyclcerView extends BaseRecyclerViewAdapter<Result,MyRecyclcerView.ViewHolder> {

    public MyRecyclcerView(int layoutResId, List<Result> data, OnRecyclerViewListener listener) {
        super(layoutResId, data, listener);
    }

    @Override
    protected void bindData(ViewHolder holder, Result data) {

    }

    public class ViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
