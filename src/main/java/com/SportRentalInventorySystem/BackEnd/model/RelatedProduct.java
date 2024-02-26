package com.SportRentalInventorySystem.BackEnd.model;

import java.io.Serializable;


import javax.persistence.*;
@Entity
@Table(name = "related_product")
public class RelatedProduct implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "related_product_id")
    private Long relatedProductId;

    public RelatedProduct() {
    	super();
    }
    
	public RelatedProduct(Product product, Long relatedProductId) {
		super();
		this.product = product;
		this.relatedProductId = relatedProductId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getRelatedProductId() {
		return relatedProductId;
	}

	public void setRelatedProductId(Long relatedProductId) {
		this.relatedProductId = relatedProductId;
	}
    
    
    
    
}