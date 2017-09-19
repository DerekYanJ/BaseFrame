package com.yqy.testframe;

import com.yqy.frame.bean.Result;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DerekYan on 2017/9/19.
 */

public interface HttpService {

    /**
     * 登录 第三方登录
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("url/")
    Observable<Result<Login>> getLogin(@FieldMap Map<String, String> params);

}
