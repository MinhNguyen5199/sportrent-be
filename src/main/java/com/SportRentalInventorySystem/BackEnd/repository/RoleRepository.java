
package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SportRentalInventorySystem.BackEnd.model.Role;
import com.SportRentalInventorySystem.BackEnd.model.ERole;

/**
 * @author Meron Seyoum
 * @Date 30-09-2022
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
