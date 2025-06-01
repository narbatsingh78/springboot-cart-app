package com.springbootcartapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.springbootcartapp.entities.CardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface SelectedItemRepository extends JpaRepository<CardItem, Integer> {

	Page<CardItem> findAllByOrderByPriorityDesc(Pageable pageable);

	Page<CardItem> findBySubCategoryId(Integer subCategoryId, Pageable pageable);

	List<CardItem> findBySubCategoryId(Integer subCategoryId);

	@Query("SELECT c FROM CardItem c Where c.subCategory.category.id=:categoryId")
	Page<CardItem> findByCategoryId(Integer categoryId, Pageable pageable);

	Page<CardItem> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
	
//	@Query("SELECT c FROM CardItem c WHERE c.subCategory.id = :subCategoryId")
//	List<CardItem> findBySubCategoryId(@Param("subCategoryId") Integer subCategoryId);

	void save(Double userRating);

	Page<CardItem> findAllByProductLaunchDateLessThanEqualOrderByPriorityDesc(LocalDate now, Pageable pageable);

	@Query("SELECT c FROM CardItem c WHERE c.subCategory.category.id = :categoryId " +"AND c.productLaunchDate <= :now " +"ORDER BY c.priority DESC")
		Page<CardItem> findByCategoryIdAndProductLaunchDateLessThanEqualOrderByPriorityDesc(
		    @Param("now") LocalDate now, 
		    @Param("categoryId") Integer categoryId, 
		    Pageable pageable);

	Page<CardItem> findBySubCategoryIdAndProductLaunchDateLessThanEqualOrderByPriorityDesc(Integer subCategoryId,LocalDate now,
			Pageable pageable);

	Page<CardItem> findByTitleContainingIgnoreCaseAndProductLaunchDateLessThanEqualOrderByPriorityDesc(String keyword,LocalDate now,
			Pageable pageable);


}