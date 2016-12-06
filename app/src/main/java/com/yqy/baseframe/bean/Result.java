package com.yqy.baseframe.bean;

/**
 * @author derekyan
 * @desc
 * @date 2016/12/6
 */

public class Result<T> {
    public int ret = 0 ;
    public String msg = "";
    public T data = null; //返回数据 可对象 可数组
}
