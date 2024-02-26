package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SportRentalInventorySystem.BackEnd.model.RelatedProduct;

@Repository
public interface RelatedProductRepository extends JpaRepository<RelatedProduct, Long> {
    // You can add custom query methods here if needed
	
	
	 // Custom query to find related products based on the product_id
    List<RelatedProduct> findByProductId(long productId);
}
