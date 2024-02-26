package com.SportRentalInventorySystem.BackEnd.controller;

/**
 *  @author  Meron Seyoum
 * @version 1.0
 * 
 * The CategoryController class is a REST controller for managing category information. 
 * It provides methods for getting, creating, updating, and deleting category information. 
 * These methods can only be accessed by users with the appropriate roles, as specified by the @PreAuthorize annotations. 
 * This class also uses the CategoryRepository to access and manipulate category data stored in a database.
 */
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import com.SportRentalInventorySystem.BackEnd.model.Category;
import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.repository.CategoryRepository;
import com.SportRentalInventorySystem.BackEnd.utility.FileUploadUtil;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * build create Category REST API
     * 
     * @param categoryDetails
     * @return Category
     */
    @PostMapping("/createCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestParam("category_No") String category_No,
            @RequestParam("category_Name") String category_Name, @RequestParam("season") String season,
            @RequestParam("category_Image") MultipartFile category_Image) throws IOException {

        Category category = new Category();
        category.setCategory_No(category_No);
        category.setCategory_Name(category_Name);
        category.setSeason(season);
        category.setCategory_Image(category_Image.getBytes());

        Category savedCategory = categoryRepository.save(category);

        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    /**
     * delete PRoduct REST API
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCategory/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not exist with id: " + id));

        categoryRepository.delete(category);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * build update Product REST API
     * 
     * @param id
     * @param categoryInfo
     * @return
     */

//    Update is not working
    @PutMapping("/categoryUpdate/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable long id,@RequestParam("category_No") String category_No,
            @RequestParam("category_Name") String category_Name, @RequestParam("season") String season,
            @RequestParam("category_Image") MultipartFile category_Image) throws IOException{
        
        Category updateCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Update fail " + id));

        updateCategory.setCategory_No(category_No);
        updateCategory.setCategory_Image(category_Image.getBytes());
        updateCategory.setCategory_Name(category_Name);
        updateCategory.setSeason(season);

        categoryRepository.save(updateCategory);
        return ResponseEntity.ok(updateCategory);
    }

    /**
     * Product search
     * 
     * @param keyWords
     * @return
     */

    @GetMapping("/searchcategory/{keyWords}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> searchCategory(@PathVariable String keyWords) {
        return new ResponseEntity<>(categoryRepository.searchByCatName(keyWords), HttpStatus.OK);
    }

    /**
     * Category information managed by admin. manager CRUD operation is done here
     * 
     * @return
     */

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCategories() {
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    /**
     * build get Category by id REST API
     * 
     * @param id
     * @return
     */
    @GetMapping("/categoryById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> getCategory(@PathVariable long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not exist with id:" + id));
        return ResponseEntity.ok(category);
    }

}
