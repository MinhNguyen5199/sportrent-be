/**
* @author  Meron seyoum
* @version 1.0
* @since   2022-09-24
* 
*/
package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SportRentalInventorySystem.BackEnd.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email); 
     
    public User findByResetPasswordToken(String token);
    
    
    
////  Search category by keyword
  @Query("select c from User c where c.firstName LIKE %?1% OR c.lastName LIKE %?1%")
  public List<User> searchByUserName(String keyWords);
  
  @Query("SELECT COUNT(u) FROM User u WHERE YEAR(u.create_at) = :year")
  Long findUserCount(@Param("year") int year);
  

  // data of user registered each week in a specific year
  @Query("SELECT u FROM User u WHERE YEAR(u.create_at) = :year")
  List<User> findNewUsers(@Param("year") int year);

//New method to retrieve users by roles
  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name <> 'ROLE_USER' AND u.status = 'Active' ")
  List<User> findUsersByRolesOtherThanUser();
  
}
