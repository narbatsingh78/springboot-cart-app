package com.springbootcartapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootcartapp.Dto.CategoryWithSubCategories;
import com.springbootcartapp.entities.Category;
import com.springbootcartapp.entities.SubCategory;
import com.springbootcartapp.repository.CategoryRepository;
import com.springbootcartapp.repository.SubCategoryRepository;

@RestController
@RequestMapping("items/category")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CategoryController {
	
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	SubCategoryRepository subCategoryRepository;
	
	
	@GetMapping("getAllCategories")
	public ResponseEntity<List<Category>> getAllCategory(){
		List<Category> categories=categoryRepository.findAll();
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/subCategories")
    public List<SubCategory> getSubCategories(@RequestParam Integer categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }
	
	 @GetMapping("/getAllCategoriesWithSubCategories")
	public ResponseEntity<List<CategoryWithSubCategories>> getAllCategoriesWithSubCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryWithSubCategories> categoryWithSubCategoriesList = new ArrayList<>();

        for (Category category : categories) {
            List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(category.getId());
            CategoryWithSubCategories categoryWithSubCategories = new CategoryWithSubCategories(category, subCategories);
            categoryWithSubCategoriesList.add(categoryWithSubCategories);
        }

        return ResponseEntity.ok(categoryWithSubCategoriesList);
    }


}
