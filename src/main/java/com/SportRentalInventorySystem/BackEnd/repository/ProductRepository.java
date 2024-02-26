package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.model.ProductProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // search by product name
    @Query("select c from Product c where c.product_Name LIKE %?1%")
    public List<Product> searchByProName(String keyWords);

//  Get all product by category ID p.id, p.product_name, p.description, p.price, c.category_name, c.season , p.product_image
//    get product and category using id
    @Query(nativeQuery = true, value = "SELECT * FROM product p LEFT JOIN category c ON c.category_id = p.category WHERE c.category_id=:category_id")
    public List<ProductProjection> productByCatId(long category_id);
    
    
    
//  get product and category using id
    @Query(nativeQuery = true, value = "SELECT * FROM product p LEFT JOIN category c ON c.category_id = p.category")
    public List<ProductProjection> productByCat();
    
///  get product and category by season
  @Query(nativeQuery = true, value = "SELECT * FROM product p LEFT JOIN category c ON c.category_id = p.category where c.season =?1")
  public List<ProductProjection> findBySeason(String season);

    
//get product data using product name
    @Query("select p from Product p where p.product_Name=:product_Name")
   public List<Product> findByProductName(String product_Name);
    
    
    //get product data with most featured 
//    SELECT MIN(product_id) AS product_id, category FROM product GROUP BY category
//    @Query(nativeQuery = true, value =" SELECT * FROM Product p JOIN Category c ON p.category = c.category_id LEFT JOIN Product p2 ON p.category = p2.category AND p.id < p2.id WHERE p2.id IS NULL GROUP BY c.category_name")
    
       @Query(nativeQuery = true, value ="select c.*, p.* FROM Product p JOIN Category c ON p.category = c.category_id LEFT JOIN Product p2 ON p.category = p2.category AND p.id < p2.id WHERE p2.id IS NULL GROUP BY c.category_name")
          public List<ProductProjection> findByTopSale();
    

   //  Get all product to be search by front end
       @Query(nativeQuery = true, value = "SELECT * FROM product p LEFT JOIN category c ON c.category_id = p.category")
       public List<ProductProjection> ProductSearch();
    
//   get images after save    
       @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.id = :productId")
       public Product getProductWithImages(@Param("productId") Long productId);
       
//       retrieve image for shopping cart
       @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.id = :productId")
      public Product getProductImage(@Param("productId") Long productId);
}
