package com.crazydevstuff.infoshare.Models;

import android.graphics.Bitmap;

public class ProductModel {
    private String productName;
    private String productDescription;
    private Bitmap productImage;
    private String seller;

    public ProductModel(String productName, String productDescription, Bitmap productImage, String seller) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.seller = seller;
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

    public String getSeller() {
        return seller;
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

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
