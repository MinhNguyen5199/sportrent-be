package com.SportRentalInventorySystem.BackEnd.controller;

import java.io.IOException;
import java.util.Calendar;

/**
 * @author  Meron Seyoum
 * @version 1.0
 * 
 * This is a Java class that defines a REST controller for managing products. It has methods for handling HTTP requests 
 * for creating, updating, deleting, and getting product information. The methods are annotated with 
 * @PostMapping, @PutMapping, @DeleteMapping, and @GetMapping, respectively, which determine 
 * the HTTP request method and the path that the method will handle. The class is also annotated with @RestController and @RequestMapping, 
 * which identify the class as a REST controller and set the base path for the controller's methods.
 */

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import com.SportRentalInventorySystem.BackEnd.model.Category;
import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.model.ProductImage;
import com.SportRentalInventorySystem.BackEnd.model.ProductList;
import com.SportRentalInventorySystem.BackEnd.model.RelatedProduct;
import com.SportRentalInventorySystem.BackEnd.repository.CategoryRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ProductListRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ProductRepository;
import com.SportRentalInventorySystem.BackEnd.repository.ReservedItemRepository;
import com.SportRentalInventorySystem.BackEnd.services.ProductService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductListRepository productListRepository;

	@Autowired
	private ProductService productService;
	
	
	Category categoryNo;

	/**
	 * This method retrieves all products from the database and sends them to the
	 * client. It requires ROLE_ADMIN authorization to access.
	 * 
	 * @return ResponseEntity containing a list of Product objects or an error
	 *         message.
	 */

	@GetMapping("/product")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getAllProducts() {
		try {
			List<Product> products = productRepository.findAll();
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error fetching products: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * build get user by id REST API
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping("/productById/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Product> getProductById(@PathVariable long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id:" + id));
		return ResponseEntity.ok(product);
	}

	// @GetMapping("/productById/{id}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public Product getProductId(long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id:" + id));
		return product;
	}

	/**
	 * Create a new product. Requires ROLE_ADMIN authorization to access.
	 * 
	 * @param categoryId     The ID of the category to which the product belongs.
	 * @param type           The type of the product.
	 * @param product_Name   The name of the product.
	 * @param description    The description of the product.
	 * @param product_status The status of the product.
	 * @param price          The price of the product.
	 * @param quantity       The quantity of the product.
	 * @param product_Image  The image of the product.
	 * @return ResponseEntity containing the created Product object or an error
	 *         message if the creation fails.
	 * @throws IOException If there is an error with the product image.
	 */
	@PostMapping("/createProduct/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Product> createProduct(@RequestParam("category_id") Long categoryId,
			@RequestParam("type") String type, @RequestParam("product_Name") String product_Name,
			@RequestParam("description") String description, @RequestParam("product_status") String product_status,
			@RequestParam("price") double price, @RequestParam("quantity") Integer quantity,
			@RequestParam("product_Image") MultipartFile product_Image) throws IOException {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		Product lastInsertedProduct = null;
		long productId = 0;
		String serialNumberCode = "";
		
		// check if a product exist by searching it product name if exist escape saving
		// and send warring message
		// Check if product already exists by name
        //Product existingProduct = (Product) productRepository.findByProductName(product_Name);
        //if (existingProduct != null) {
        //throw new RuntimeException("Product already exists with the name " + product_Name)
        //  }
		
		Product product = new Product();
		product.setCategory(category);
		product.setType(type);
		product.setProduct_Name(product_Name);
		product.setDescription(description);
		product.setProduct_status(product_status);
		product.setPrice(price);
		product.setQuantity(quantity);
		product.setProduct_Image(product_Image.getBytes());

		Product savedProduct = productRepository.save(product);

		productRepository.flush();

//      insert product list using the as last inserted row primary id

		// current insert object(row) primary id
		productId = product.getId();
		// get the product row as object from table using id
		lastInsertedProduct = getProductId(productId);

		for (int itemNo = 1; itemNo <= lastInsertedProduct.getQuantity(); itemNo++) {

			serialNumberCode = category.getCategory_No() + "-" + lastInsertedProduct.getType() + "-" + itemNo;

			ProductList productList = new ProductList(lastInsertedProduct, lastInsertedProduct.getProduct_status(),
					lastInsertedProduct.getDescription(), serialNumberCode);

			productListRepository.save(productList);
		}

		return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	}

	/**
	 * Update an existing product. Requires ROLE_ADMIN authorization to access.
	 * 
	 * @param id             The ID of the product to update.
	 * @param categoryId     The ID of the new category to which the product
	 *                       belongs.
	 * @param type           The new type of the product.
	 * @param product_Name   The new name of the product.
	 * @param description    The new description of the product.
	 * @param product_status The new status of the product.
	 * @param price          The new price of the product.
	 * @param quantity       The new quantity of the product.
	 * @param product_Image  The new image of the product.
	 * @return ResponseEntity containing the updated Product object or an error message if the update fails.
	 * @throws IOException If there is an error with the product image.
	 */

	@PutMapping("/productUpdate/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestParam("category_id") Long categoryId,
			@RequestParam("type") String type, @RequestParam("product_Name") String product_Name,
			@RequestParam("description") String description, @RequestParam("product_status") String product_status,
			@RequestParam("price") double price, @RequestParam("quantity") Integer quantity,
			@RequestParam("product_Image") MultipartFile product_Image) throws IOException {

		Product updateProduct = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product Update fail " + id));

		updateProduct.setDescription(description);
//        updateProduct.setCategory(productInfo.getCategory());
		updateProduct.setPrice(price);
		updateProduct.setProduct_status(product_status);
		updateProduct.setProduct_Name(product_Name);
		updateProduct.setQuantity(quantity);
		updateProduct.setType(type);

		updateProduct.setProduct_Image(product_Image.getBytes());

		productRepository.save(updateProduct);
		return ResponseEntity.ok(updateProduct);
	}

	/**
	 * delete Product REST API
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deleteProduct/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id: " + id));
		productRepository.delete(product);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Product search
	 * 
	 * @param keyWords
	 * @return
	 */
	@GetMapping("/searchProduct/{keyWords}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> SearchProdut(@PathVariable String keyWords) {
		return new ResponseEntity<>(productRepository.searchByProName(keyWords), HttpStatus.OK);
	}

	/**
	 * Get productList data
	 * 
	 * @return
	 */
	@GetMapping("/productListById/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getAllProductList(@PathVariable long id) {

		return new ResponseEntity<>(productListRepository.findByProductListId(id), HttpStatus.OK);
	}

	/**
	 * End point to add related products to a product
	 * 
	 * @param id
	 * @param relatedProductIds
	 * @return
	 */

	@PostMapping("/addRelatedProduct/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> addRelatedProducts(@PathVariable Long id, @RequestBody List<Long> relatedProductIds) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}

		Set<RelatedProduct> relatedProducts = relatedProductIds.stream().map(relatedProductId -> {
			RelatedProduct relatedProduct = new RelatedProduct();
			relatedProduct.setProduct(product);
			relatedProduct.setRelatedProductId(relatedProductId);
			// Set other related product attributes if needed
			return relatedProduct;
		}).collect(Collectors.toSet());

		product.getRelatedProducts().addAll(relatedProducts);
		productRepository.save(product);
		return ResponseEntity.ok("Related products added to the product.");
	}

	/**
	 * End point to get related products for a product
	 * 
	 * @param productId
	 * @return
	 */
	@GetMapping("/relatedProductById/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<Product>> getRelatedProductsById(@PathVariable("id") long productId) {
		Set<Product> relatedProducts = productService.getRelatedProductsById(productId);
		return ResponseEntity.ok(relatedProducts);
	}

//End point to add images to a product
	@PostMapping("/images/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Product> addProductImages(@PathVariable Long productId,
			@RequestParam("images") MultipartFile[] images) throws IOException {
		Product product = productService.addProductImages(productId, images);
		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// End point to delete an image from a product
	@DeleteMapping("/{productId}/images/{imageId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Product> deleteProductImage(@PathVariable Long productId, @PathVariable Long imageId) {
		Product product = productService.deleteProductImage(productId, imageId);
		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/imageListById/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Long productId) {
        Product product = productRepository.getProductWithImages(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductImage> productImages = product.getProductImages();
        return ResponseEntity.ok(productImages);
    }
	

}
