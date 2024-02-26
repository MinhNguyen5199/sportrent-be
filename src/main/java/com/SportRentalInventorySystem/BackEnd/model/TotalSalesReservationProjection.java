package com.SportRentalInventorySystem.BackEnd.model;

public class TotalSalesReservationProjection {

	    private Double totalSales;
	    private Long totalReservations;
		public int length;

	    public TotalSalesReservationProjection(Double totalSales, Long totalReservations) {
	        this.totalSales = totalSales;
	        this.totalReservations = totalReservations;
	    }

	    // Getters and setters
	    public Double getTotalSales() {
	        return totalSales;
	    }

	    public void setTotalSales(Double totalSales) {
	        this.totalSales = totalSales;
	    }

	    public Long getTotalReservations() {
	        return totalReservations;
	    }

	    public void setTotalReservations(Long totalReservations) {
	        this.totalReservations = totalReservations;
	    }
	

}
