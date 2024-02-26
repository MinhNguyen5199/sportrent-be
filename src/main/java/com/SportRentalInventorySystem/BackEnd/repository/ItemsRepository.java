package com.SportRentalInventorySystem.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SportRentalInventorySystem.BackEnd.model.ReservedItem;

public interface ItemsRepository extends JpaRepository<ReservedItem, Long> {

}
