package com.example.lianxi;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Goods {

    private String title;
    private double bargainPrice;
    private String images;
    @Id
    private long pid;
    private String price;
    @Generated(hash = 1596443592)
    public Goods(String title, double bargainPrice, String images, long pid,
            String price) {
        this.title = title;
        this.bargainPrice = bargainPrice;
        this.images = images;
        this.pid = pid;
        this.price = price;
    }
    @Generated(hash = 1770709345)
    public Goods() {
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getBargainPrice() {
        return this.bargainPrice;
    }
    public void setBargainPrice(double bargainPrice) {
        this.bargainPrice = bargainPrice;
    }
    public String getImages() {
        return this.images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public long getPid() {
        return this.pid;
    }
    public void setPid(long pid) {
        this.pid = pid;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    
}
