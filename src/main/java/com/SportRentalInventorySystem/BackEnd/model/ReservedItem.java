package com.SportRentalInventorySystem.BackEnd.model;

/**
 * @author  Meron Seyoum
* @version 1.0
* @since   2022-11-19
 * 
 * 
 * This is a Java class for a ReservedItem entity that is annotated for use with the Java Persistence API (JPA). 
 * The @Entity and @Table annotations are used to specify that this class should be mapped to a reserved_item table in a database. 
 * The @Id and @GeneratedValue annotations are used to specify that the id field is the primary key of the table, 
 * and that its values should be generated automatically by the database. 
 * The @ManyToOne and @OneToMany annotations are used to specify relationships between this ReservedItem entity and other entities. 
 * The @Column annotations are used to specify that the fields of this class should be mapped to columns in the reserved_item table. 
 * This class also contains a hashCode method and an equals method that are used to compare ReservedItem objects based on their id values. 
 */


import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reserved_item")
public class ReservedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "amount")
    private long amount;
    
    @Column(name = "on_sale_price")
    private long onSalePrice;

    @Column(name = "quantity")
    private long quantity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Reservation reservation;

    public ReservedItem() {
    }

    public ReservedItem(long amount, long quantity, long onSalePrice, Product product, Reservation reserve) {
        super();
        this.amount = amount;
        this.quantity = quantity;
        this.onSalePrice= onSalePrice;
        this.product = product;
        this.reservation = reserve;
    }

   

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getOnSalePrice() {
		return onSalePrice;
	}

	public void setOnSalePrice(long onSalePrice) {
		this.onSalePrice = onSalePrice;
	}
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            ReservedItem reservedItem = (ReservedItem) o;
//            return Objects.equals(rItem_id, reservedItem.rItem_id);
//        }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
