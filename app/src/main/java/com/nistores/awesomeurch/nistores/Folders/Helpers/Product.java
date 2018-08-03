package com.nistores.awesomeurch.nistores.Folders.Helpers;

/**
 * Created by Awesome Urch on 29/07/2018.
 * POJO class for passing my product JSON
 */

public class Product {
    private String pname;
    private String pphoto;
    private String pprice;
    private String store_uid;
    private String views;

    public String getTitle() {
        return pname;
    }

    public void setTitle(String title) {
        this.pname = title;
    }

    public String getImage() {
        return pphoto;
    }

    public void setImage(String image) {
        this.pphoto = image;
    }

    public String getPrice() {
        return pprice;
    }

    public void setPrice(String price) {
        this.pprice = price;
    }

    public String getStore_uid() {
        return store_uid;
    }

    public void setStore_uid(String price) {
        this.store_uid = price;
    }

    public String getViews(){ return views; }

    public void setViews(String views){
        this.views = views;
    }
}
