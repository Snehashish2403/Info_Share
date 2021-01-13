package com.crazydevstuff.infoshare.Models;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_products")
public class ProductModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String productName;
    
    private String productDescription;

    private String productImage;

    private String sellerName;

    private Integer productPrice;

    private String sellerImage;

    public ProductModel(String productName, String productDescription, String productImage, String sellerName,Integer productPrice, String sellerImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.sellerName = sellerName;
        this.sellerImage = sellerImage;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getSellerName() {
        return sellerName;
    }

}
