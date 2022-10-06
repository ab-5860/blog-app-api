package com.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.CategoryDTO;
import com.blog.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO)
    {
            CategoryDTO createCategory = this.categoryService.createCategory(categoryDTO);

            return new ResponseEntity<CategoryDTO>(createCategory, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,@PathVariable Integer catId)
    {
        System.out.println("Inside category controller");
        CategoryDTO updateCategory = this.categoryService.updateCategory(categoryDTO,catId);
        return ResponseEntity.ok(updateCategory);
    }

    //delete
    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId)
    {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted successfully", true),HttpStatus.OK);
    }


    //get
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Integer catId)
    {
        CategoryDTO category = this.categoryService.getCategory(catId);
        return ResponseEntity.ok(category);
    }


    //get all
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getCategories()
    {
        List<CategoryDTO> categories = this.categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

}
