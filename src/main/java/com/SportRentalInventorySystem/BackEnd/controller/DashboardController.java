package com.SportRentalInventorySystem.BackEnd.controller;


import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SportRentalInventorySystem.BackEnd.model.Reservation;
import com.SportRentalInventorySystem.BackEnd.model.User;
import com.SportRentalInventorySystem.BackEnd.repository.ItemsRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ProductRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ReservationRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ReservedItemRepository;
import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;

/** @author Meron Seyoum
* @version 1.0
*/

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/AdminDashboard")
public class DashboardController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservedItemRepository reserveItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemsRepository itemRepository;

    
    Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);

    /**
     * Endpoint to fetch data for the admin dashboard.
     * Combines data from different sources: chart data, total sales, total products rented, and latest transactions.
     */
    @GetMapping("/dashboard-data")
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        try {
            // Initialize the dashboard data map
            Map<String, Object> dashboardData = new HashMap<>();

            // Fetch and add chart data
            dashboardData.put("chartData", getProductData());

            // Fetch and add total sales for this year
            dashboardData.put("totalSales", getTotalSalesThisYear().getBody());

            // Fetch and add total products rented for this year
            dashboardData.put("totalProductsRented", getProductsRentedThisYear().getBody());
           
            // Fetch and add User head count for this year
            dashboardData.put("userData", getUsersCount().getBody());
            
            // Fetch and add Reservation count for this year
            dashboardData.put("reservationCount", getReservationCount().getBody());
           
            // Fetch and add latest transactions
            dashboardData.put("latestTransactions", getLatestTransaction().getBody());
            
         // Fetch and add new Users for this year
            dashboardData.put("newUsers", getNewRegisteredUsers().getBody());
           
            // Fetch and add Employee profile
            dashboardData.put("employeeProfile", getEmployeeProfile().getBody());
         
            
            // Return the complete dashboard data
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            // Log the exception
            // Use a logging framework like SLF4J
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Fetches and transforms product data for the chart.
     * The data includes monthly rental and sales information.
     */
    private Map<String, Object> getProductData() {
        List<Map<String, Object>> data = new ArrayList<>();

        // Fetch the data from the database using your repository or service
        List<Reservation> reservations = reservationRepository.findAll(); // Fetch the reservations from the database

        // Determine the current year and month
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth firstMonthOfYear = YearMonth.of(currentYearMonth.getYear(), 1);

        // Transform the Reservation objects into the desired format
        for (Reservation reservation : reservations) {
            if (reservation.getStartDate() != null) { // Check if startDate is not null
                YearMonth reservationYearMonth = YearMonth.from(reservation.getStartDate());

                if (!reservationYearMonth.isBefore(firstMonthOfYear) && !reservationYearMonth.isAfter(currentYearMonth)) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("Month", reservation.getStartDate().getMonth().name()); // Assuming you want the month name as "name"

                    if ("Sales".equals(reservation.getReservation_Status())) {
                        item.put("Sales", reservation.getTotalPrice());
                    } else {
                        item.put("Rent", reservation.getTotalPrice());
                    }

                    data.add(item);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("Data", data);
        // Log the result or use a logging framework
        return result;
    }

    /**
     * Fetches the total sales for the current year.
     */
    private ResponseEntity<Double> getTotalSalesThisYear() {
        Double totalSales = reservationRepository.getTotalSalesThisYear();
        return ResponseEntity.ok(totalSales);
    }

    /**
     * Fetches the latest transactions.
     */
    private ResponseEntity<?> getLatestTransaction() {
        return new ResponseEntity<>(reservationRepository.latestTransactions(), HttpStatus.OK);
    }

    /**
     * Fetches the total count of products rented for the current year.
     */
    private ResponseEntity<Long> getProductsRentedThisYear() {
      
        Long productCount = reserveItemRepository.countProductsRentedThisYear(currentYear);

        return ResponseEntity.ok(productCount);
    }
    /**
     * Fetches User head count information for the current year. 
     * 
     * @return
     */
    
    public ResponseEntity<?> getUsersCount() {
    	
    	Long userCount=userRepository.findUserCount(currentYear);
        return  ResponseEntity.ok(userCount);
    }
    
    /**
     * Fetches total reservation count for the current year. 
     * 
     * @return
     */
    
    public ResponseEntity<Long> getReservationCount() {
    	Long reservationCount =reservationRepository.findReservationCount(currentYear);
    	
        return ResponseEntity.ok(reservationCount);
    }
    
    /**
     * Fetches User create_at date to create a bar for weekly new customers registered information for the current year. 
     * 
     * @return
     */
    public ResponseEntity<List<User>> getNewRegisteredUsers() {
        List<User> newUsers = userRepository.findNewUsers(currentYear);
        return new ResponseEntity<>(newUsers, HttpStatus.OK);
    }
    
 // New endpoint to retrieve users with roles other than "ROLE_USER"
   
    public ResponseEntity<List<User>> getEmployeeProfile() {
    	
        List<User> newUsers = userRepository.findUsersByRolesOtherThanUser();
        return new ResponseEntity<>(newUsers, HttpStatus.OK);
    }
}
