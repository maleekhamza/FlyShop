package com.example.myapplication;
public class ExampleItem {
    private String mImageUrl;
    private String mtitre;
    private  String mdate;
    private String mdescription;
    private  String mprice;


    public ExampleItem(String imageUrl, String titre, String date,String description,String price) {
        mImageUrl = imageUrl;
        mtitre = titre;
        mdescription =description;
        mdate = date;
        mprice =price;
    }

    public String getdescription() {
        return mdescription;
    }

    public String getprice() {
        return mprice;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitre() {
        return mtitre;
    }

    public String getDate() {
        return mdate;
    }
}