package com.yqy.frame.http;


import com.yqy.frame.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.lang.String.valueOf;

/**
 * @author derekyan
 * @desc 封装的 RxJava + Retrofit 请求
 * @date 2016/12/6
 */

public class HttpRequest {
    private long timeout = 5;//超时时间
    protected Retrofit mRetrofit;
    private HttpService mHttpService;

    private String baseUrl = "没有配置baseUrl";//接口基地址

    /**
     * 接口基地址
     * @param baseUrl
     */
    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 超时时间 单位s
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    protected long getTimeout() {
        return timeout;
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
        return SingletonHolder.INSTANCE;
    }

    /*public HttpRequest() {
        OkHttpClient.Builder mBuilder = new OkHttpClient().newBuilder();
        mBuilder.connectTimeout(timeout, TimeUnit.SECONDS); //超时时间 单位:秒
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
                .baseUrl(baseUrl) //基地址 可配置到gradle
                .build();
        mHttpService = mRetrofit.create(HttpService.class);
    }*/

    /**
     * 封装切换线程
     *
     * @param o
     * @param s
     * @param <T>
     */
    protected  <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 请求数据统一进行预处理
     *
//     * @param <T>
     */
   /* private class ResultFunc<T> implements Func1<Result<T>, T> {
        @Override
        public T call(Result<T> result) {
            if (result.ret != 200) {
                //主动抛异常  会自动进去OnError方法
                try {
                    JSONObject errorJson = new JSONObject();
                    errorJson.put("errorCode",result.ret+"");
                    errorJson.put("errorMsg",result.msg);
                    throw new ApiException(errorJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    throwException("-1","加载失败");
                }
            }
            return result.data;
        }
    }*/

    protected void throwException(String errorCode, String errorMsg){
        try {
            JSONObject errorJson = new JSONObject();
            errorJson.put("errorCode", errorCode);
            errorJson.put("errorMsg",errorMsg);
            throw new ApiException(errorJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            throwException("-1","加载失败");
        }
    }

    /**
     * 打印请求链接
     * @param params 请求参数
     */
    protected void printRequestUrl(Map<String, String> params){
        /**    打印个地址   **/
        StringBuffer sb = new StringBuffer(getBaseUrl());
        Set<String> set =  params.keySet();
        for(String str:set){
            sb.append(str + "=" + params.get(str) + "&");
        }
        sb = sb.deleteCharAt(sb.length()-1);
        L.e("requestParams",params.toString());
        L.e("requestUrl",sb.toString());
    }

    /**
     * 普通的获取数据 Object可替换要的类型
     * @param subscriber
     * @param params
     */

    /*public void getResult(Subscriber<Object> subscriber, Map<String, String> params) {
        toSubscribe(mHttpService.getResult(params).map(new ResultFunc<Object>()), subscriber);
    }*/

    /**
     * 上传文件
     * @param params 参数
     * @param file 文件
     * @param mCallback 回调函数
     */
    public void uploadFile(final Map<String, String> params, File file, Callback mCallback) {
        final String url = baseUrl;
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("uploadedfile", file.getName(), body);
        }
        if (params != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : params.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(mCallback);
    }
}
