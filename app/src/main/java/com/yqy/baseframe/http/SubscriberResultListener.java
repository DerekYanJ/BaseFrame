package com.yqy.baseframe.http;

/**
 * @author derekyan
 * @desc
 * @date 2016/12/6
 */

public interface SubscriberResultListener<T> {
    /**
     * 请求结果
     * @param t  结果类型 Result->data
     * @param requestId 请求标识
     */
    void onNext(T t, int requestId);

    /**
     *
     * @param errorCode
     * @param msg
     */
    void onError(int errorCode,String msg);
}
