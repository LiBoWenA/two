package com.example.lianxi;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Fresco.initialize(this);
        ImageLoader.getInstance().init(
                new ImageLoaderConfiguration.Builder(this)
                        .memoryCacheSizePercentage(10)
                        .discCacheSize(50 * 1024 * 1024)
                        .defaultDisplayImageOptions(
                                new DisplayImageOptions.Builder()
                                        .showImageOnFail(R.mipmap.ic_launcher)
                                        .showImageOnLoading(R.mipmap.ic_launcher)
                                        .cacheInMemory(true)
                                        .cacheOnDisk(true)
                                        .build()
                        )
                        .build()
        );

        UMConfigure.init(this,"5a12384aa40fa3551f0001d1","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }


}
