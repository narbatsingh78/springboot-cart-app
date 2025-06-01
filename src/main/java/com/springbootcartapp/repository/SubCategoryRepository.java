package com.springbootcartapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springbootcartapp.entities.Category;
import com.springbootcartapp.entities.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer>{
	
	Optional<SubCategory> findBySubCategoryNameAndCategory(String subCategoryName, Category category);

	List<SubCategory> findByCategoryId(Integer categoryId);


	Optional<SubCategory> findByIdAndCategory(Integer subCategoryId, Category foundCategory);

}
