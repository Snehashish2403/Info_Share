package com.crazydevstuff.infoshare.Models;

import android.graphics.Bitmap;

public class ProductModel {
    private String productName;
    private String productDescription;
    private Bitmap productImage;
    private String sellerName;
    private int productPrice;
    private Bitmap sellerImage;

    public ProductModel(){

    }

    public ProductModel(String productName, String productDescription, Bitmap productImage, String sellerName,int productPrice, Bitmap sellerImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.sellerName = sellerName;
        this.sellerImage = sellerImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Bitmap getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(Bitmap sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Bitmap getProductImage() {
        return productImage;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductImage(Bitmap productImage) {
        this.productImage = productImage;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
