package com.SportRentalInventorySystem.BackEnd.model;

public interface ProductProjection {

    long getId();

    String getCategory_Name();

    String getProduct_Name();

    String getDescription();

    double getPrice();

    String getSeason();

    byte[] getProduct_Image();

    double getQuantity();
    
    double getAvailable_quantity();

   // void setProduct_Image(byte[] productImageBytes);

 
}
