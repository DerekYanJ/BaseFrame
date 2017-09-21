package com.yqy.frame.frame;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DerekYan on 2017/9/21.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public BaseViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }
}
