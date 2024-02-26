package com.SportRentalInventorySystem.BackEnd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Meron Seyoum
 * @version 1.0
 * @since 2022-09-24
 * 
 *        This code is a Java class representing a product in a database.
 *        The @Entity and @Table annotations indicate that this class is a JPA
 *        entity
 *        and should be mapped to a database table with the name "product",
 *        respectively.
 *        The @Id and @GeneratedValue annotations specify that the id field is
 *        the primary key for the entity and should be generated automatically
 *        by the database.
 *        The @Column annotations map the fields in this class to columns in the
 *        "product" table in the database.
 *        The @ManyToOne and @OneToMany annotations specify relationships
 *        between this entity and other entities in the database. This class
 *        also provides getters and setters for each field,
 *        as well as a toString() method that prints the fields of the class in
 *        a human-readable format for testing and debugging.
 */

@Entity
@Table(name = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "product_name")
    private String product_Name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private double quantity;

    
    @Column(nullable = false, columnDefinition = "double default 0")
    private double available_quantity;
    
    @Column(name = "status")
    private String product_status;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "productImage")
   
    private byte[] product_Image;
    

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductList> productList;
    
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RelatedProduct> relatedProducts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

  
    public Product() {

    }

    public Product(long id, String type, String product_Name, String description, double price,
            double quantity, double available_quantity, String product_status, Category category, byte[] product_Image) {
        super();
        this.id = id;
        this.type = type;
        this.product_Name = product_Name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.available_quantity= available_quantity;
        this.product_status = product_status;
        this.category = category;
        this.product_Image = product_Image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct_Name() {
        return product_Name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    
    
    public double getAvailable_quantity() {
		return available_quantity;
	}

	public void setAvailable_quantity(double available_quantity) {
		this.available_quantity = available_quantity;
	}

	public void setProduct_Name(String product_Name) {
        this.product_Name = product_Name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getProduct_Image() {
        return product_Image;
    }

    public void setProduct_Image(byte[] product_Image) {
        this.product_Image = product_Image;
    }
    
    public Set<RelatedProduct> getRelatedProducts() {
		return relatedProducts;
	}

	public void setRelatedProducts(Set<RelatedProduct> relatedProducts) {
		this.relatedProducts = relatedProducts;
	}
	
	

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages.clear();
        if (productImages != null) {
            this.productImages.addAll(productImages);
            for (ProductImage productImage : productImages) {
                productImage.setProduct(this);
            }
        }
    }
    
    public void addProductImage(ProductImage productImage) {
        productImages.add(productImage);
        productImage.setProduct(this);
    }

    public void removeProductImage(ProductImage productImage) {
        productImages.remove(productImage);
        productImage.setProduct(null);
    }

	@Override
    public String toString() {
        return "Product [id=" + id + ", type=" + type + ", product_Name=" + product_Name + ", description="
                + description
                + ", price=" + price + ", quantity=" + quantity + ",available_quantity ="+ available_quantity+", product_status="
                + product_status + ", category=" + category + ", product_Image=" + product_Image + "]";
    }

}
