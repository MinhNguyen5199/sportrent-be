package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SportRentalInventorySystem.BackEnd.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long>  {

          //get UserAddress data using ForeignId
	@Query("SELECT a FROM UserAddress a WHERE a.user.id = :user_id")
           public Optional<UserAddress> findByForeignId(Long user_id);
}
