package com.yqy.testframe;

/**
 * Created by DerekYan on 2017/9/20.
 */

public class MResult<T> {
    public int status = 0 ;
    public String desc = "";
    public T data = null; //返回数据 可对象 可数组
}
