package com.SportRentalInventorySystem.BackEnd.model;

/**
* @author  Meron Seyoum
* @version 1.0
* @since   2022-09-24
*
* This code is a Java class called Role that represents a role in a system. 
* The class has fields for the role's id and name, which is an enumerated type (ERole).
* The class also has methods for setting and getting the values of these fields. 
* The class is marked with @Entity and @Table annotations,
* which means it is a JPA entity and is mapped to a database table.
* The name field is marked with the @Enumerated and @Column annotations,
* which specify that it should be stored as a string in the database.
*/


import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}