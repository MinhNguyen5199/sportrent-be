package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SportRentalInventorySystem.BackEnd.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Fetch distinct reservations for a user with additional information
    @Query(nativeQuery = true, value = "SELECT DISTINCT r.* FROM reserved r INNER JOIN users u ON r.user = u.id WHERE r.user =:user GROUP BY r.user")
    public List<Reservation> pickupInfo(long user);

    // Fetch reservation history by user id
    @Query(nativeQuery = true, value = "SELECT  r.* FROM reserved r INNER JOIN users u ON r.user = u.id WHERE r.user =:user")
    public List<Reservation> orderHistory(long user);

    // Find the last reservation record
    @Query(nativeQuery = true, value = "SELECT * FROM reserved r ORDER BY date_Stamp_Date DESC LIMIT 1")
    public Reservation findLastRecord();

    // Search reservations by reservation code or pickup full name
    @Query(nativeQuery = true, value = "SELECT * FROM reserved r WHERE r.reservation_Code LIKE CONCAT('%', :searchTerm, '%') OR r.pickup_full_name LIKE CONCAT('%', :searchTerm, '%') ORDER BY r.date_Stamp_Date")
    public List<Reservation> searchRecord(@Param("searchTerm") String searchTerm);

    // Calculate the total sales for the current year
    @Query(nativeQuery = true, value = "SELECT SUM(r.total_Price) FROM reserved r WHERE YEAR(r.date_Stamp_Date) = YEAR(CURDATE()) AND MONTH(r.date_Stamp_Date) BETWEEN 1 AND MONTH(CURDATE())")
    public Double getTotalSalesThisYear();

    // Fetch the latest transactions (excluding 'Cancelled' and 'Concept' reservations)
    @Query(nativeQuery = true, value = "SELECT * FROM reserved WHERE reservation_status NOT IN ('Cancelled', 'Concept') ORDER BY date_stamp_date DESC LIMIT 10")
    public List<Reservation> latestTransactions();

    // Count the number of reservations for a specific year
    @Query(nativeQuery = true, value = "SELECT COUNT(r.id) FROM reserved r WHERE YEAR(r.date_Stamp_Date) = :year")
    Long findReservationCount(@Param("year") int year);

}
