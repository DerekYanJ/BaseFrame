package com.yqy.frame.http;

/**
 * @author derekyan
 * @desc
 * @date 2016/12/6
 */

public class ApiException extends RuntimeException {

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @return
     */
    private static String getApiExceptionMessage(String msg){
        return msg;
    }
}
