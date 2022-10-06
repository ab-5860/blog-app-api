package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDTO;
import com.blog.repositories.CategoryRepo;
import com.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        
        Category cat = this.modelMapper.map(categoryDTO, Category.class);
        Category addedCat = this.categoryRepo.save(cat);

        return this.modelMapper.map(addedCat, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId) {
        
        System.out.println("Inside category Service implementaion");
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()->new  ResourceNotFoundException(
                                                                            "Category ", "Category Id",categoryId
        ));

        cat.setCategoryTitle(categoryDTO.getCategoryTitle());;
        cat.setCategoryDescription(categoryDTO.getCategoryDescription());

        Category updatedcat = this.categoryRepo.save(cat);
        return this.modelMapper.map(updatedcat, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(
                                                "Category ", "category id", categoryId));

        this.categoryRepo.delete(cat);                                   
        
    }

    @Override
    public CategoryDTO getCategory(Integer categoryId) {
        
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()->
                                                                new ResourceNotFoundException("Category", 
                                                                "category id", categoryId));
        return this.modelMapper.map(cat,CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getCategories() {
        
        List<Category> categories = this.categoryRepo.findAll();
        List<CategoryDTO> catDtos = categories.stream().map((cat)-> this.modelMapper.map(cat,CategoryDTO.class)).collect(Collectors.toList());
        
        return catDtos;
    }
    
}
