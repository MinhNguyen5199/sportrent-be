package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SportRentalInventorySystem.BackEnd.model.ProductProjection;
import com.SportRentalInventorySystem.BackEnd.model.Reservation;
import com.SportRentalInventorySystem.BackEnd.model.ReservedItem;
import com.SportRentalInventorySystem.BackEnd.model.ReservedItemProjection;
import com.SportRentalInventorySystem.BackEnd.model.User;

public interface ReservedItemRepository extends JpaRepository<Reservation, Long> {

    void save(ReservedItem itemReserve);
   
  
//fetch reserved item by reservation id
            @Query(nativeQuery = true, value = "SELECT i.*, p.product_name, p.product_image\r\n" + 
            		" FROM reserved_item i \r\n" + 
            		" inner join reserved r ON  i.reservation_id = r.id \r\n" + 
            		"inner join product p ON i.product_id = p.id where i.reservation_id=:r_id")
    public List<ReservedItemProjection> findByReservationId(long r_id);
            

//fetch the total number of products rented this year
            @Query("SELECT COUNT(ri) FROM ReservedItem ri INNER JOIN ri.reservation r WHERE YEAR(r.startDate) = :year")
            Long countProductsRentedThisYear(@Param("year") int year);

           
}
