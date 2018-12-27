package com.example.lianxi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BinderThread;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lianxi.adapter.ViewPagerAdapter;
import com.example.lianxi.bean.TitleBean;
import com.example.lianxi.persenter.IPersenterImpl;
import com.example.lianxi.view.IView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements IView {
    private String path = "http://www.zhaoapi.cn/product/getProductDetail";
    private IPersenterImpl iPersenter;
    @BindView(R.id.pager)
    ViewPager pager;
    private TextView tTitle,tPrice;
    @BindView(R.id.login)
    RadioButton button;
    private List<String> list;
    private SimpleDraweeView draweeView;
    private Unbinder bind;
    private int j=0;
    private ViewPagerAdapter adapter;
   private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pager.setCurrentItem(j%list.size());
            j++;
            sendEmptyMessageDelayed(0,3000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adapter = new ViewPagerAdapter(this);
        bind = ButterKnife.bind(this);
 //       EventBus.getDefault().register(this);
        //获取资源ID
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    private void init() {
        tTitle = findViewById(R.id.text_title);
        tPrice = findViewById(R.id.text_price);
        draweeView = findViewById(R.id.simview);
        iPersenter = new IPersenterImpl(this);

        //点击按钮进行第三方登录
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","aaa");
                UMShareAPI umShareAPI = UMShareAPI.get(LoginActivity.this);
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        String name  = map.get("screen_name");
                        //获取头像
                        String img  = map.get("profile_image_url");
                        Log.i("TAG",img);
                        Uri path = Uri.parse(img);
                        draweeView.setImageURI(path);


                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }
        });

        Intent intent = getIntent();
        int pid = intent.getIntExtra("pid", 0);


        //无线轮播

        pager.setAdapter(adapter);

        Map<String,String> map = new HashMap<>();
        map.put("pid",pid+"");
        iPersenter.showRequestData(path,map,TitleBean.class);

    }

    @Override
    public void startRequestData(Object data) {
        TitleBean bean = (TitleBean) data;
        tTitle.setText(bean.getData().getTitle());
        tPrice.setText(bean.getData().getPrice()+"");
        String images = bean.getData().getImages();
        String[] split = images.split("\\|");
        list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        //开始轮播图片
        adapter.setList(list);
        handler.sendEmptyMessageDelayed(0,3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
