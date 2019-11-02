package com.inducesmile.androidpayexample.entities;


public class ProductObject {

    private int productId;

    private String productName;

    private int productImage;

    private String productDescription;

    private double productPrice;

    private String productSize;


    public ProductObject(int productId, String productName, int productImage, String productDescription, double productPrice, String productSize) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productSize = productSize;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductSize() {
        return productSize;
    }




    @Override
    public String toString() {
        return "Product id and name: " + productId + " " + productName;
    }
}
