package com.yqy.frame.frame;

import android.view.View;

import com.yqy.frame.bean.Result;
import com.yqy.frame.listener.OnRecyclerViewListener;

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

    public class ViewHolder extends BaseViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
