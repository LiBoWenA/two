package com.example.lianxi.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lianxi.LoginActivity;

import cn.jpush.android.api.JPushInterface;

public class Myreciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            //打开自定义的Activity
            Intent i = new Intent(context, LoginActivity.class);
            i.putExtra("pid","45");
            context.startActivity(i);
        }
    }
}
