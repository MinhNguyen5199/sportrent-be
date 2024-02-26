package com.SportRentalInventorySystem.BackEnd.model;

public interface ReservedItemProjection {
	long getId();

	byte[] getProduct_Image();

	String getProduct_Name();

	double getAmount();

	long getQuantity();
	long getReservation_id();

}
