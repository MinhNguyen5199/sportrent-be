package com.SportRentalInventorySystem.BackEnd.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "product_image")
public class ProductImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Lob
    @Column(name = "image_data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

	public ProductImage( byte[] imageData, Product product) {
		super();	
		this.imageData = imageData;
		this.product = product;
	}

	public ProductImage() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

    // Constructors, getters, and setters for the fields
}
