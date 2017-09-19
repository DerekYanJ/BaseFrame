package com.yqy.testframe;

import com.yqy.frame.utils.L;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

/**
 * Created by DerekYan on 2017/9/19.
 */

public class HttpRequest extends com.yqy.frame.http.HttpRequest {
    private HttpService mHttpService;

    public void getLogin(Subscriber<Login> subscriber, Map<String, String> params) {
        toSubscribe(mHttpService.getLogin(params).map(new ResultFunc<Login>()), subscriber);
    }

    /**
     * 打印请求链接
     * @param params 请求参数
     */
    public void printRequestUrl(Map<String, String> params){
        /**    打印个地址   **/
        StringBuffer sb = new StringBuffer(getBaseUrl());
        Set<String> set =  params.keySet();
        for(String str:set){
            sb.append(str + "=" + params.get(str) + "&");
        }
        sb = sb.deleteCharAt(sb.length()-1);
        L.e("okhttpParams",params.toString());
        L.e("okhttpRequestUrl",sb.toString());
    }

    /**
     * 在访问HttpService时创建单例
     */
    private static class SingletonHolder {
        private static final HttpRequest INSTANCE = new HttpRequest();
    }

    /**
     * 获取单例
     * 既实现了线程安全，又避免了同步带来的性能影响
     * @return
     */
    public static HttpRequest getInstance() {
        return HttpRequest.SingletonHolder.INSTANCE;
    }

    public HttpRequest(){
        setBaseUrl("基地址");
        OkHttpClient.Builder mBuilder = new OkHttpClient().newBuilder();
        mBuilder.connectTimeout(getTimeout(), TimeUnit.SECONDS); //超时时间 单位:秒
        //DEBUG 测试环境添加日志拦截器
        if(L.isShow){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mBuilder.addInterceptor(loggingInterceptor);
        }
        //添加一个设置header拦截器
        mBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //可以设置和添加请求头数据
                Request mRequest = chain.request().newBuilder()
                        .build();
                return chain.proceed(mRequest);
            }
        });
        mRetrofit = new Retrofit.Builder()
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl()) //基地址 可配置到gradle
                .build();
        mHttpService = mRetrofit.create(HttpService.class);
    }

}
