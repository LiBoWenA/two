package com.example.lianxi.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lianxi.MyApp;
import com.example.lianxi.okhttp.IcallBack;
import com.example.lianxi.okhttp.MyCallBack;
import com.example.lianxi.okhttp.OkHttpUtils;

import java.util.Map;

public class IModelImpl implements IModel {

    public static boolean hasNetWork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isAvailable();
    }
    @Override
    public void requestData(String path, Map<String, String> params, Class clazz, final MyCallBack myCallBack) {
        Context context = OkHttpUtils.getAppContext();

        if (hasNetWork(context)) {
            OkHttpUtils.getInstance().postEnqueue(path, params, clazz, new IcallBack() {
                @Override
                public void failed(Exception e) {
                    myCallBack.sucess(e);
                }

                @Override
                public void sucess(Object data) {
                    myCallBack.sucess(data);
                }
            });
        }else {
            myCallBack.sucess("网络请求失败");
        }
    }

}
