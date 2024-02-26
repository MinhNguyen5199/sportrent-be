package com.SportRentalInventorySystem.BackEnd.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.model.ProductList;
import com.SportRentalInventorySystem.BackEnd.model.Reservation;
import com.SportRentalInventorySystem.BackEnd.model.ReservedItem;
import com.SportRentalInventorySystem.BackEnd.model.User;
import com.SportRentalInventorySystem.BackEnd.repository.ItemsRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ProductRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ReservationRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ReservedItemRepository;
import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;
//import com.stripe.net.APIResource.RequestMethod;

import net.bytebuddy.utility.RandomString;
import org.springframework.transaction.annotation.Transactional;
/**
 * This code is a REST API that allows users to make reservations for products
 * and add products to a shopping cart. It also has functionality for sending
 * emails and searching for products. The code uses the Spring framework and
 * uses the @RestController and
 * 
 * @RequestMapping annotations to map incoming HTTP requests to specific handler
 *                 methods. It also uses the @Autowired annotation to inject
 *                 dependencies and the @CrossOrigin annotation to enable
 *                 cross-origin resource sharing (CORS). The code also uses
 *                 the @PreAuthorize annotation to enable access control.
 * 
 * @author Meron Seyoum
 * @version 1.0
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/Reservation")
public class ReservationController {

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

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Creates a new reservation for a user.
	 *
	 * @param id                 The user ID.
	 * @param reservationDetails The reservation details.
	 * @return ResponseEntity containing the created Reservation.
	 */
//	@PostMapping("/makeReservation/{id}")
//	public ResponseEntity<Reservation> makeReservation(@PathVariable long id,
//			@RequestBody Reservation reservationDetails) {
//		// Generate a reservation code
//		String Reservation_Code = RandomString.make(6);
//
//		// Create the reservation
//		Reservation reserve = userRepository.findById(id).map(user -> {
//			reservationDetails.setUser(user);
//			reservationDetails.setReservation_Code(Reservation_Code);
//			return reservationRepository.save(reservationDetails);
//		}).orElseThrow(() -> new RuntimeException("Failed to create new Reservation"));
//
//		return new ResponseEntity<>(reserve, HttpStatus.CREATED);
//	}
	@PostMapping("/makeReservation/{user_id}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Reservation> makeReservation(@PathVariable long user_id,
	        @RequestBody Reservation reservationDetails) {
	    try {
	        // Generate a reservation code
	        String reservationCode = RandomString.make(6);

	        // Create the reservation
	        Reservation reservation = userRepository.findById(user_id).map(user -> {
	            reservationDetails.setUser(user);
	            reservationDetails.setReservation_Code(reservationCode);
	            return reservationRepository.save(reservationDetails);
	        }).orElseThrow(() -> new IllegalArgumentException("Failed to create new Reservation"));

	        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
	    } catch (IllegalArgumentException e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	/**
	 * Adds items to the shopping cart for a reservation.
	 *
	 * @param id          The reservation ID.
	 * @param email       The user's email.
	 * @param reserveItem List of reserved items.
	 * @return ResponseEntity with a success status.
	 * @throws Exception 
	 * @throws IOException        If there is an IO error.
	 * @throws MessagingException If there is a messaging error.
	 */
//	@PostMapping("/addCart/{id}/{email}")
//	public ResponseEntity<?> addCart(@PathVariable long id, @PathVariable String email,
//			@RequestBody List<ReservedItem> reserveItem) throws IOException, MessagingException {
//		// Get the last entered reservation
//		Reservation reserve = reservationRepository.findLastRecord();
//
//		// Save each reserved item to the database
//		for (int i = 0; i < reserveItem.size(); i++) {
//			ReservedItem itemReserve = new ReservedItem(reserveItem.get(i).getAmount(),
//					reserveItem.get(i).getQuantity(), reserveItem.get(i).getOnSalePrice(),
//					getProductId(reserveItem.get(i).getProduct().getId()), reserve);
//
//			reserveItemRepository.save(itemReserve);
//		}
//
//		 // Send email confirmation
//	    sendEmail(email, reserve, reserve.getReservation_Code(), reserveItem);
//
//
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}

	
	@PostMapping("/addCart/{user_id}/{email}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> addCart(@PathVariable long user_id, @PathVariable String email,
	        @RequestBody List<ReservedItem> reserveItems) throws Exception {
	    try {
	        // Get the last entered reservation
	        Reservation reserve = reservationRepository.findLastRecord();

	        // Update the product quantity and save each reserved item to the database
	        for (ReservedItem itemReserve : reserveItems) {
	            Product product = itemReserve.getProduct();
	            long productId = product.getId();
	            long reservedQuantity = itemReserve.getQuantity();
	            long amount = itemReserve.getAmount();
	            long onSalePrice = itemReserve.getOnSalePrice();

	            // Retrieve the product from the database
	            Optional<Product> optionalProduct = productRepository.findById(productId);
	            if (optionalProduct.isPresent()) {
	                Product existingProduct = optionalProduct.get();
	                double currentQuantity = existingProduct.getAvailable_quantity();
	                double updatedQuantity = currentQuantity - (double) reservedQuantity;

	                // Check if the available quantity is sufficient
	                if (updatedQuantity < 0) {
	                    throw new IllegalArgumentException("Insufficient quantity for product: " + product.getProduct_Name());
	                }

	                // Update the product quantity
//	                existingProduct.setQuantity(updatedQuantity);
	                existingProduct.setAvailable_quantity(updatedQuantity);

	                // Save the updated product
	                productRepository.save(existingProduct);
	            } else {
	                throw new IllegalArgumentException("Product not found: " + productId);
	            }

	            // Create a new ReservedItem entity for each item in the cart
	            ReservedItem item = new ReservedItem();
	            item.setAmount(amount);
	            item.setQuantity(reservedQuantity);
	            item.setOnSalePrice(onSalePrice);
	            item.setProduct(product);
	            item.setReservation(reserve);

	            // Save the reserved item
	            reserveItemRepository.save(item);
	        }
	        // Send email confirmation
	        sendEmail(email, reserve, reserveItems);

	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (IllegalArgumentException e) {
	        throw e;
	    } catch (Exception e) {
	        throw e;
	    }
	}

	
	/**
	 * Updates the details of a reservation.
	 *
	 * @param id              The reservation ID.
	 * @param reservationInfo The updated reservation details.
	 * @return ResponseEntity containing the updated Reservation.
	 */
	@PutMapping("/updateReservation/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable("id") long id,
			@RequestBody Reservation reservationInfo) {
		Reservation existingReservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

		// Update reservation details
		existingReservation.setStartDate(reservationInfo.getStartDate());
		existingReservation.setEndDate(reservationInfo.getEndDate());
		existingReservation.setPayment_Option(reservationInfo.getPayment_Option());
		// Update other reservation details

		Reservation updatedReservation = reservationRepository.save(existingReservation);
		return ResponseEntity.ok(updatedReservation);
	}

	// Other controller methods

	// Helper methods and other dependencies

	/**
	 * Retrieves the product ID from the product's database table. the product Id of
	 * a product that going to be add to the reservation item
	 *
	 * @param id The product ID.
	 * @return The product's database ID.
	 */

	public Product getProductId(long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id:" + id));
		return product;
	}

	/**
	 * update the reservation status to either picked up or reservation Cancelled
	 */
	// update the reservation status
	@PutMapping("/updateReservationStatus/{id}")
	public ResponseEntity<Reservation> updateReservationStatus(@PathVariable long id,
			@RequestParam("reservation_status") String reservationStatus) {

		return reservationRepository.findById(id).map(reservation -> {
			reservation.setReservation_Status(reservationStatus);
			Reservation updatedReservation = reservationRepository.save(reservation);

			return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
		}).orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
	}

	/**
	 * Retrieve data
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getPickupInfo/{id}")
	public ResponseEntity<?> getPickupInfo(@PathVariable long id) {

		return new ResponseEntity<>(reservationRepository.pickupInfo(id), HttpStatus.OK);
	}

	/**
	 * Retrieved data by user id
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping("/getOrderHistory/{id}")
	public ResponseEntity<?> getOrderHistory(@PathVariable long id) {

		return new ResponseEntity<>(reservationRepository.orderHistory(id), HttpStatus.OK);
	}

	/**
	 * Fetch reservation info by id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getReservationById/{id}")
	public ResponseEntity<?> getReservationById(@PathVariable long id) {

		return new ResponseEntity<>(reservationRepository.findById(id), HttpStatus.OK);
	}

	/**
	 * fetch reserved item by id reservation id
	 */
	@GetMapping("/getReservedItemById/{id}")
	public ResponseEntity<?> getReservedItemById(@PathVariable long id) {

		return new ResponseEntity<>(reserveItemRepository.findByReservationId(id), HttpStatus.OK);
	}

	/**
	 * get all reservation
	 */

	@GetMapping("/getReservation")
	public ResponseEntity<?> getReservation() {

		return new ResponseEntity<>(reservationRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/getReservationLastRecord")
	public ResponseEntity<?> getReservationLastRecord() {

		return new ResponseEntity<>(reservationRepository.findLastRecord(), HttpStatus.OK);
	}

	/**
	 * Search Reservation using reservation_Code or customer name
	 */
	@GetMapping("/searchReservation/{searchTerm}")
	public ResponseEntity<?> searchReservation(@PathVariable String searchTerm) {
		return new ResponseEntity<>(reservationRepository.searchRecord(searchTerm), HttpStatus.OK);
	}

	/**
	 * Delete reserved Item using its unique id
	 * 
	 * 
	 */
	@DeleteMapping("/deleteReservedItemById/{id}")
	public ResponseEntity<HttpStatus> deleteReservedItemById(@PathVariable long id) {

		ReservedItem itemReserve = itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("reserved Item not exist with id: " + id));
		itemRepository.delete(itemReserve);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	/**
	 * Sends an email confirmation for the reservation.
	 *
	 * @param email           The recipient's email.
	 * @param reservation     The reservation details.
	 * @param reservationCode The reservation code.
	 * @throws MessagingException If there is a messaging error.
	 */
	private void sendEmail(String recipientEmail, Reservation reserved, List<ReservedItem> reserveItems)
        throws MessagingException, UnsupportedEncodingException {
    // Calculate subtotal
    double subtotal = 0.0;
    for (ReservedItem reserveItem : reserveItems) {
        double itemAmount = reserveItem.getAmount();
        subtotal += itemAmount;
    }
    // Calculate VAT rate
    double vatRate = subtotal * 0.05;

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("sportsrentReset@gmail.com", "Sports Rent Support");
    helper.setTo(recipientEmail);

    String subject = "Reservation Confirmation Email";

    StringBuilder contentBuilder = new StringBuilder();
    contentBuilder.append("<p>Reservation Confirmation</p>")
            .append("<html><body>")
            .append("<table style=\"width: 80%;\">")
            .append("<tr><td colspan=\"2\"><strong>Reservation Code:</strong><h5> ").append(reserved.getReservation_Code()).append("</h5></td><td ><strong>Date: </strong>")
            .append(reserved.getDate_Stamp_Date()).append("</td></tr>")
            .append("<tr><td colspan=\"4\"><strong>Address:</strong> ").append(reserved.getAddress()).append(" ")
            .append(reserved.getCity()).append(" ").append(reserved.getProvince()).append(" ")
            .append(reserved.getCountry()).append("</td></tr>")
            .append("<tr><td colspan=\"4\"></td></tr>")
            .append("<tr><td colspan=\"4\"><h2>Reservation Detail</h2></td></tr>")
            .append("<tr><td colspan=\"4\"><strong>Pickup: </strong>").append(reserved.getStartDate()).append("</td></tr>")
            .append("<tr><td colspan=\"4\"><strong>Return: </strong>").append(reserved.getEndDate()).append("</td></tr><br>")
            .append("<tr><td colspan=\"4\"><strong>Status:</strong> ").append(reserved.getReservation_Status()).append("</td></tr>")
            .append("<tr><td colspan=\"4\"><strong>Name:</strong> ").append(reserved.getPickupFullName()).append("</td></tr>")
            .append("<tr><td colspan=\"4\"><strong>Contact:</strong> ").append(recipientEmail).append("</td></tr>")
            .append("<tr ><td><strong>Item Name</strong></td><td><strong>Price</strong></td><td><strong>Quantity</strong></td><td><strong>Amount</strong></td></tr>");

    // Add reserved items to the table
    for (ReservedItem reserveItem : reserveItems) {
        contentBuilder.append("<tr >")
                .append("<td>").append(reserveItem.getProduct().getProduct_Name()).append("</td>")
                .append("<td>").append(reserveItem.getOnSalePrice()).append("</td>")
                .append("<td>").append(reserveItem.getQuantity()).append("</td>")
                .append("<td>").append(reserveItem.getAmount()).append("</td>")
                .append("</tr>");
    }

    // Add subtotal, VAT, and grand total rows
    contentBuilder.append("<tr ><td colspan=\"2\"></td><td><strong>Subtotal</strong></td><td><strong>").append(subtotal).append("</strong></td></tr>")
            	  .append("<tr ><td colspan=\"2\"></td><td ><strong>VAT rate </strong></td><td>(5%)</td></tr>")
                  .append("<tr ><td colspan=\"2\"></td><td ><strong>VAT</strong></td><td>").append(vatRate).append("</td></tr>")
                  .append("<tr ><td colspan=\"2\"></td><td ><strong>Grand Total</strong></td><td><strong>").append(reserved.getTotalPrice()).append("</strong></td></tr>");

    contentBuilder.append("</table></body></html>");

    String content = contentBuilder.toString();

    helper.setSubject(subject);
    helper.setText(content, true);

    mailSender.send(message);
}

}
