package com.SportRentalInventorySystem.BackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SportRentalInventorySystem.BackEnd.model.Category;
import com.SportRentalInventorySystem.BackEnd.model.CategoryProjection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
//  Select category by season for land page
  @Query("select  c, COUNT(p.id) as num_products \r\n" + 
  		"from Category c LEFT JOIN Product p ON c.category_id = p.category \r\n" + 
  		"where c.season =?1 GROUP BY c.category_id, c.season")
  public List<Object[]> findBySeason(String season);
   
//  @Query("select DISTINCT  c.category_id, c.category_Name, p.description, c. season from Category c INNER JOIN Product p ON c.category_id= p.category where c.season =?1")
//  public List<categoryProjectionInterface> findBySeason(String season);
//  
//    Search category by keyword
    @Query("select c  from Category c where c.category_Name LIKE %?1%")
    public List<Category> searchByCatName(String keyWords);
    
//    fetch data for category list in navbar to display category list
    
    @Query(nativeQuery = true, value = "SELECT c FROM category c")
    public List<Category> findCategoryList();
    
    
}
