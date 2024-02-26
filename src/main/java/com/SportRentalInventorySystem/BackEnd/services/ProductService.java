package com.SportRentalInventorySystem.BackEnd.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.model.ProductImage;
import com.SportRentalInventorySystem.BackEnd.model.RelatedProduct;
import com.SportRentalInventorySystem.BackEnd.repository.ProductRepository;
import com.SportRentalInventorySystem.BackEnd.repository.RelatedProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RelatedProductRepository relatedProductRepository;

	@Transactional
	public Product getProductById(long productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Transactional
	public void addRelatedProductsToProduct(long productId, List<Long> relatedProductIds) {
		Product product = productRepository.findById(productId).orElse(null);
		if (product != null) {
			List<RelatedProduct> relatedProducts = relatedProductRepository.findAllById(relatedProductIds);
			product.setRelatedProducts(relatedProducts.stream().collect(Collectors.toSet()));
			productRepository.save(product);
		}
	}

//    retrieve related product of a selected product using productId
	public Set<Product> getRelatedProductsById(long productId) {
		// Find all RelatedProduct objects with the given productId as
		// related_product_id
		List<RelatedProduct> relatedProducts = relatedProductRepository.findByProductId(productId);

		// Collect the related_product_id values
		Set<Long> relatedProductIds = relatedProducts.stream().map(RelatedProduct::getRelatedProductId)
				.collect(Collectors.toSet());

		// Find all Product objects with the collected related_product_id values
		List<Product> products = productRepository.findAllById(relatedProductIds);

		return new HashSet<>(products);
	}
	

	public Product addProductImages(Long productId, MultipartFile[] images) throws IOException {
	    Product product = getProductById(productId);
	    if (product != null) {
	        for (MultipartFile image : images) {
	            // Process and save the image data to the ProductImage entity
	            byte[] imageData = image.getBytes();
	            ProductImage productImage = new ProductImage();
	            productImage.setImageData(imageData);
	            product.addProductImage(productImage);
	        }
	        return productRepository.save(product);
	    } else {
	        return null;
	    }
	}

    public Product deleteProductImage(Long productId, Long imageId) {
        Product product = getProductById(productId);
        if (product != null) {
            // Find the image by imageId in the product's images and remove it
            List<ProductImage> productImages = product.getProductImages();
            for (Iterator<ProductImage> iterator = productImages.iterator(); iterator.hasNext();) {
                ProductImage productImage = iterator.next();
                if (productImage.getId() == imageId) {
                    iterator.remove();
                }
            }
            return productRepository.save(product);
        } else {
            return null;
        }
    }
    
  
}
