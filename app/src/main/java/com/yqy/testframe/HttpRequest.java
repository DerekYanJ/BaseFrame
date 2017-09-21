package com.yqy.testframe;

import com.yqy.frame.http.ApiException;
import com.yqy.frame.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by DerekYan on 2017/9/19.
 */

class HttpRequest extends com.yqy.frame.http.HttpRequest {
    private HttpService mHttpService;

    void getLogin(Subscriber<Login> subscriber, Map<String, String> params) {
        toSubscribe(mHttpService.getLogin(getSpecialParams(params)).map(new MResultFunc<Login>()), subscriber);
    }

    /**
     * 将请求参数做特殊处理 比如添加公共请求参数
     * @param params 参数
     * @return 处理后的参数
     */
    private Map<String, String> getSpecialParams(Map<String, String> params){
        if(L.isShow) printRequestUrl(params);
        return params;
    }

    private class MResultFunc<T> implements Func1<MResult<T>, T> {

        @Override
        public T call(MResult<T> tResult) {
            //status 视情况而定 与后台规定好的请求成功的字段
            //此例 status == 0 时为请求成功
            if (tResult.status != 0) {
                //主动抛异常  会自动进去OnError方法
                try {
                    JSONObject errorJson = new JSONObject();
                    errorJson.put("errorCode",tResult.status+"");
                    errorJson.put("errorMsg",tResult.desc);
                    throw new ApiException(errorJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    throwException("-1","加载失败");
                }
            }
            return tResult.data;
        }
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
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl()) //基地址 可配置到gradle
                .build();
        mHttpService = mRetrofit.create(HttpService.class);
    }

    private class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody,Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }

}
