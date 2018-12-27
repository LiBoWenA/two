package com.example.lianxi.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.lianxi.MyApp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {
    private static  volatile OkHttpUtils instance;
    private OkHttpClient mClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    //单例
    public static OkHttpUtils getInstance(){
        if (instance == null){
            synchronized (OkHttpUtils.class){
                instance = new OkHttpUtils();
            }
        }
        return instance;
    }

    public static Context getAppContext() {
        return MyApp.context;
    }

    private OkHttpUtils(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }


    //get异步
    public void getEnqueue(String path, final Class clazz, final MyCallBack myCallBack){

        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();

        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.sucess(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final Object o = new Gson().fromJson(json, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myCallBack.sucess(o);
                    }
                });
            }
        });
    }
    //post请求
    public void postEnqueue(String path, Map<String,String> params, final Class clazz, final IcallBack myCallBack){
        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String,String> entry:params.entrySet()) {
            builder.add(entry.getKey(),entry.getValue());
        }

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();

        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.sucess(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final Object o = new Gson().fromJson(json, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myCallBack.sucess(o);
                    }
                });
            }
        });
    }
}
