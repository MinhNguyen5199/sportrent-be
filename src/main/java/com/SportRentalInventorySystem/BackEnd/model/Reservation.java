package com.SportRentalInventorySystem.BackEnd.model;

/**
* @author  Meron Seyoum
* @version 1.0
* @since   2022-11-19
 * 
 * 
 * This is a Java class for a Reservation entity that is annotated for use with the Java Persistence API (JPA). 
 * The @Entity and @Table annotations are used to specify that this class should be mapped to a reserved table in a database. 
 * The @Id and @GeneratedValue annotations are used to specify that the id field is the primary key of the table,
 *  and that its values should be generated automatically by the database.
 *   The @ManyToOne and @OneToMany annotations are used to specify relationships between this Reservation entity and other entities.
 *  The @Column annotations are used to specify that the fields of this class should be mapped to columns in the reserved table.
 * 
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reserved")
public class Reservation {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user")
    private User user;
    
    @Column(name = "pickupFullName")
    private String pickupFullName;
    
    @Column(name = "address")
    private String address;
     
    @Column(name = "city")
    private String city;
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "country")
    private String country;
   
    @Column(name = "zip")
    private String zip;
 
    @Column(name = "duration")
    private Integer duration;
    
    @Column(name = "startDate")
    private LocalDate startDate;
    
    @Column(name = "endDate")
    private LocalDate endDate;
    
    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "date_Stamp_Date")
    private LocalDateTime date_Stamp_Date;

    @Column(name = "payment_Option")
    private String payment_Option;

    @Column(name = "reservation_Status")
    private String reservation_Status;
    
    @Column(name = "reservation_Code")
    private String reservation_Code;
    
   
    public Reservation(User user, String address, String city, String province, String country, String zip,
            Integer duration, LocalDate startDate, LocalDate endDate, Double totalPrice, LocalDateTime date_Stamp_Date,
            String payment_Option, String reservation_Status, String reservation_Code) {
        super();
        this.user = user;
        this.address = address;
        this.city = city;
        this.province = province;
        this.country = country;
        this.zip = zip;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.date_Stamp_Date = date_Stamp_Date;
        this.payment_Option = payment_Option;
        this.reservation_Status = reservation_Status;
        this.reservation_Code = reservation_Code;
    }

    public Reservation() {
        super();
        this.date_Stamp_Date = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPickupFullName() {
        return pickupFullName;
    }

    public void setPickupFullName(String pickupFullName) {
        this.pickupFullName = pickupFullName;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDate_Stamp_Date() {
        return date_Stamp_Date;
    }

    public void setDate_Stamp_Date(LocalDateTime date_Stamp_Date) {
        this.date_Stamp_Date = date_Stamp_Date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPayment_Option() {
        return payment_Option;
    }

    public void setPayment_Option(String payment_Option) {
        this.payment_Option = payment_Option;
    }

    public String getReservation_Status() {
        return reservation_Status;
    }

    public void setReservation_Status(String reservation_Status) {
        this.reservation_Status = reservation_Status;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



    public String getReservation_Code() {
        return reservation_Code;
    }

    public void setReservation_Code(String reservation_Code) {
        this.reservation_Code = reservation_Code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Reservation reserve = (Reservation) o;
        return Objects.equals(id, reserve.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}