package com.SportRentalInventorySystem.BackEnd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
*
* @author  Meron seyoum
* @version 1.0
* 
*This is a public controller class for the Sport Rental Inventory System. It contains a number of REST API methods that can be accessed 
*by users to retrieve information about products and categories. These methods include getSummerCategory, 
*which returns all categories for the summer season, and getWinterCategory, which returns all categories for the winter season. 
*Other methods include getAllProduct, which returns all products, and getProductBySeason, which returns all products for a given season. 
*Additionally, there are methods for searching for products, retrieving pickup and drop off information, and more.
*
*/


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import com.SportRentalInventorySystem.BackEnd.model.Category;
import com.SportRentalInventorySystem.BackEnd.model.CategoryProjection;
import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.model.ProductImage;
import com.SportRentalInventorySystem.BackEnd.model.ProductProjection;
import com.SportRentalInventorySystem.BackEnd.model.User;
import com.SportRentalInventorySystem.BackEnd.model.UserAddress;
import com.SportRentalInventorySystem.BackEnd.repository.CategoryRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ProductRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ReservationRepository;
import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;
import com.SportRentalInventorySystem.BackEnd.services.ProductService;
/**It also allows for the retrieval of
* summer and winter categories, top-selling products, and products by category
* and season.
*/
@CrossOrigin(origins = "*" ) 
@RestController
@RequestMapping("/api/public")
public class PublicController {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductService productService;

    
    // access to all public content

    // retrieve all category for summer or winter
    @GetMapping("/{season}Category")
    public ResponseEntity<?> getCategoryBySeason(@PathVariable String season) {
        List<Object[]> result = categoryRepository.findBySeason(season);
        
       
     // Modify response data to include count of products for each category
        List<Map<String, Object>> responseData = new ArrayList<>();
        for (Object[] row : result) {
            Category category = (Category) row[0];
            Long numProducts = (Long) row[1];
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category_id", category.getCategory_id());
            categoryData.put("category_Name", category.getCategory_Name());
            categoryData.put("season", category.getSeason());
            categoryData.put("categroy_No", category.getCategory_No());
            categoryData.put("category_Image", category.getCategory_Image());
            categoryData.put("num_products", numProducts);
            responseData.add(categoryData);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    // Retrieve Product information and send to client
    @GetMapping("/TopSaleProduct")
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>(productRepository.findByTopSale(), HttpStatus.OK);
    }
 
//    Get all summer product
    @GetMapping("/ProductBySeason/{season}")
    public ResponseEntity<?> getProductBySeason(@PathVariable String season) {
        return new ResponseEntity<>(productRepository.findBySeason(season), HttpStatus.OK);
    }
    
    @GetMapping("/category")
    public ResponseEntity<?> getAllCategorys(){
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }
    
    @GetMapping("/categoryList")
    public ResponseEntity<?> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

//  Get product by Category ID
  @GetMapping("/getProductByCategoryID/{id}")
  public ResponseEntity<?> getProductByCat(@PathVariable long id) {
      return new ResponseEntity<>(productRepository.productByCatId(id), HttpStatus.OK);
  }
  
  
//Get product by Category ID
@GetMapping("/getProductByCategory")
public ResponseEntity<?> getProductByCategory() {
// List<ProductProjection> products = productRepository.productByCat();
    
//    return new ResponseEntity<>(products, HttpStatus.OK);
    return new ResponseEntity<>(productRepository.productByCat(), HttpStatus.OK);
}
    
  // Retrieve Product information and send to client
  @GetMapping("/searchProduct")
  public ResponseEntity<?> ProductSearch() {
      return new ResponseEntity<>(productRepository.ProductSearch(), HttpStatus.OK);
  }
    
  @GetMapping("/getPickupInfo/{id}")
  public ResponseEntity<?> getPickupInfo(@PathVariable long id) {

      return new ResponseEntity<>(reservationRepository.pickupInfo(id), HttpStatus.OK);
  }
  @PutMapping("/update/{id}")
 
  public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User userDetails, UserAddress userAddress) {
      User updateUser = userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + id));
      updateUser.setFirstName(userDetails.getFirstName());
      updateUser.setLastName(userDetails.getLastName());
      updateUser.setPhoneNumber(userDetails.getPhoneNumber());
     
      
      System.out.print(updateUser);

   // Set child reference(userAddress) in parent entity(user)
//      updateUser.setUserAddress(userAddress);
//
//      // Set parent reference(user) in child entity(userAddress)
//      userAddress.setUser(updateUser);

      // Save Parent Reference (which will save the child as well)
      
      userRepository.save(updateUser);

      return ResponseEntity.ok(updateUser);
  }
  
  /**
   * End point to get related products for a product
   * @param productId
   * @return
   */
   @GetMapping("/relatedProductById/{id}")
   public ResponseEntity<Set<Product>> getRelatedProductsById(@PathVariable("id") long productId) {
       Set<Product> relatedProducts = productService.getRelatedProductsById(productId);
       return ResponseEntity.ok(relatedProducts);
   }
  
   @GetMapping("/imageListById/{productId}")
	
   public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Long productId) {
       Product product = productRepository.getProductWithImages(productId);
       if (product == null) {
           return ResponseEntity.notFound().build();
       }

       List<ProductImage> productImages = product.getProductImages();
       return ResponseEntity.ok(productImages);
   }
}
