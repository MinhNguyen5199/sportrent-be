package com.SportRentalInventorySystem.BackEnd.model;

public interface CategoryProjection {

	long getCategory_id();

	String getCategory_Name();

	String getSeason();
	
	byte[] getCategory_Image() ;
}
