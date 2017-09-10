package com.yqy.frame.http;

import com.yqy.frame.bean.Result;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author derekyan
 * @desc
 * @date 2016/12/6
 */

public interface HttpService {

    /**
     *
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("url/")
    Observable<Result<Object>> getResult(@FieldMap Map<String, String> params);

}
