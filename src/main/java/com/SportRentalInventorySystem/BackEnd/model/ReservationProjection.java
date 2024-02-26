package com.SportRentalInventorySystem.BackEnd.model;

import java.time.LocalDate;

public interface ReservationProjection {

	public long getId();

	public String getReservation_code();

	public LocalDate getDate_stamp_date();

	 public LocalDate getEndDate();

	  public LocalDate getStartDate() ;

	public String getReservation_Status();

	  public Double getTotalPrice();

	  public long getAmount() ;

	public double getQuantity();

	public String getProduct_Name();

	public byte[] getProduct_Image();

}
