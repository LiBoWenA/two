package com.example.lianxi;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.lianxi.adapter.BeanAdapter;
import com.example.lianxi.bean.Bean;
import com.example.lianxi.green.DaoMaster;
import com.example.lianxi.green.DaoSession;
import com.example.lianxi.green.GoodsDao;
import com.example.lianxi.persenter.IPersenterImpl;
import com.example.lianxi.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IView{
    private IPersenterImpl iPersenter;
    private String path = "http://www.zhaoapi.cn/product/searchProducts";
    private int page;
    private final int TYPE_MOREN = 0;
    private BeanAdapter adapter;
    private XRecyclerView gridRecyclerView;
    private Map<String, String> pamars;
    GoodsDao mGoodsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        page = 1;
        //获取资源ID
        init();
    }

    private void initData() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"goods");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();
        mGoodsDao = daoSession.getGoodsDao();
    }

    private void init() {
        gridRecyclerView = findViewById(R.id.xview);
        iPersenter = new IPersenterImpl(this);
        gridLayout();

    }

    private void gridLayout() {
        adapter = new BeanAdapter(this,false);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        gridRecyclerView.setLayoutManager(manager);
        gridRecyclerView.setAdapter(adapter);
        //设置刷新加载
        gridRecyclerView.setLoadingMoreEnabled(true);
        gridRecyclerView.setPullRefreshEnabled(true);
        //设置监听
        gridRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadingData();
            }

            @Override
            public void onLoadMore() {
                loadingData();
            }
        });
        pamars = new HashMap<>();
        pamars.put("keywords","手机");
        pamars.put("page",page+"");
        pamars.put("sort",TYPE_MOREN +"");
        iPersenter.showRequestData(path,pamars,Bean.class);
        adapter.setOnClick(new BeanAdapter.OnClick() {
            @Override
            public void click(int position) {
                int pid = adapter.getData(position);
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
        adapter.setOnClick(new BeanAdapter.OnLongClick() {
            @Override
            public void click(int position) {
                adapter.removeItem(position);
            }
        });

    }


    private void loadingData() {
        pamars = new HashMap<>();
        pamars.put("keywords","手机");
        pamars.put("page",page+"");
        pamars.put("sort",TYPE_MOREN +"");
        iPersenter.showRequestData(path,pamars,Bean.class);
    }

    @Override
    public void startRequestData(Object data) {

        if (data instanceof Bean) {
            Bean bean = (Bean) data;
            List<Bean.DataBean> data1 = bean.getData();
            //添加
            for (int i = 0; i < data1.size(); i++) {
                String title = data1.get(i).getTitle();
                double bargainPrice = data1.get(i).getBargainPrice();
                String images = data1.get(i).getImages();
                int pid = data1.get(i).getPid();
                String price = data1.get(i).getPrice();
                Goods goods = new Goods();
                goods.setBargainPrice(bargainPrice);
                goods.setPid((long) pid);
                goods.setImages(images);
                goods.setPrice(price);
                goods.setTitle(title);

                mGoodsDao.insertOrReplace(goods);
            }

            if (page == 1) {
                adapter.setData(bean.getData());
            } else {
                adapter.addData(bean.getData());
            }
            page++;
            gridRecyclerView.refreshComplete();
            gridRecyclerView.loadMoreComplete();
        }else{
            List<Bean.DataBean> dataBeans=new ArrayList<>();
            //查询
            List<Goods> goods1 = mGoodsDao.queryBuilder().list();
            for (Object o:goods1) {
                if(o instanceof Goods){
                    Goods goods= (Goods) o;
                    Bean.DataBean bean = new Bean.DataBean();
                    bean.setPid((int) goods.getPid());
                    bean.setTitle(goods.getTitle());
                    bean.setPrice(goods.getPrice());
                    bean.setBargainPrice(goods.getBargainPrice());
                    bean.setImages(goods.getImages());
                    dataBeans.add(bean);
                }
                Log.i("TAG","....."+o.toString());
                adapter.setData(dataBeans);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPersenter.onDestory();
    }
}
