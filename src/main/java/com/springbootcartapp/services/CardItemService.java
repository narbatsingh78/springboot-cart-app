package com.springbootcartapp.services;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.CustomUserDetails;
import com.springbootcartapp.entities.CustomerCartItems;
import com.springbootcartapp.entities.SubCategory;
import com.springbootcartapp.repository.AddInCartItemRepository;
import com.springbootcartapp.repository.SelectedItemRepository;
import com.springbootcartapp.repository.SubCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class CardItemService {
	
	@Autowired
	private SelectedItemRepository selectedItemRepository;

	@Autowired
	private AddInCartItemRepository addInCartItemRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;
	


	@Value("${image.upload.dir}")
    private String uploadDir;

	
	public Page<CardItem> getAllItems(String role,int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    if(role.equals("USER")) {
	    	 Page<CardItem> cardItemPage = selectedItemRepository.findAllByProductLaunchDateLessThanEqualOrderByPriorityDesc(LocalDate.now(),pageable);
	 	    return cardItemPage;
		}else {
		 Page<CardItem> cardItemPage = selectedItemRepository.findAllByOrderByPriorityDesc(pageable);
		 return cardItemPage;
		}
	   
	   
	}
	
	public Page<CardItem> getCardByCategoryId(String role,Integer categoryId,Integer page,Integer size) {
		if(role.equals("USER")) {
			Pageable pageable = PageRequest.of(page, size);
			Page<CardItem> cardItems = selectedItemRepository.findByCategoryIdAndProductLaunchDateLessThanEqualOrderByPriorityDesc(LocalDate.now(),categoryId,pageable);
			return cardItems;
		}else {
			Pageable pageable = PageRequest.of(page, size);
			Page<CardItem> cardItems = selectedItemRepository.findByCategoryId(categoryId,pageable);
			return cardItems;
		}
	}
	
	public Page<CardItem> getCardBySubCategoryId(String role,Integer subCategoryId,Integer page,Integer size) {
		if(role.equals("USER")) {
			Pageable pageable = PageRequest.of(page, size);
			Page<CardItem> cardItems = selectedItemRepository.findBySubCategoryIdAndProductLaunchDateLessThanEqualOrderByPriorityDesc(subCategoryId,LocalDate.now(),pageable);
			return cardItems;	
		}else {
			Pageable pageable = PageRequest.of(page, size);
			Page<CardItem> cardItems = selectedItemRepository.findBySubCategoryId(subCategoryId,pageable);
			return cardItems;	
		}
	}
	
	public Page<CardItem> getCardItemsByTitleSearch(String role,String keyword, Integer pageNumber, Integer pageSize) {
		if(role.equals("USER")) {
			Pageable pageable=PageRequest.of(pageNumber, pageSize);
			Page<CardItem> cardItemPage=selectedItemRepository.findByTitleContainingIgnoreCaseAndProductLaunchDateLessThanEqualOrderByPriorityDesc(keyword,LocalDate.now(),pageable);
			System.out.println("By Keyword..."+cardItemPage.getContent());
			return cardItemPage;
		}else {
			Pageable pageable=PageRequest.of(pageNumber, pageSize);
			Page<CardItem> cardItemPage=selectedItemRepository.findByTitleContainingIgnoreCase(keyword,pageable);
			System.out.println("By Keyword..."+cardItemPage.getContent());
			return cardItemPage;

		}
		
	}
	
	public List<CustomerCartItems> getAllCartItemsByUser(CustomUserDetails customUserDetails) {
		System.out.println("CustomUserDetails...."+customUserDetails);
		int userId = customUserDetails.getUserId();
		List<CustomerCartItems> customerCartItems = addInCartItemRepository.findCartItemsByUserId(userId);
		return customerCartItems;
	}

	// Insert New  CardItem By Admin 
	
	public void addCardItem(String title, String description, Integer stockQuantity, Double price, Integer categoryId,
			Integer subCategoryId,String productLaunchDate,MultipartFile image,CustomUserDetails customUserDetails) throws IOException {
		System.out.println("Date..."+productLaunchDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.parse(productLaunchDate, formatter);
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); 
        }
        String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = uploadPath.resolve(imageName); 
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
		
        SubCategory foundSubCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

		CardItem cardItem = new CardItem();
		cardItem.setTitle(title);
		cardItem.setDescription(description);
		cardItem.setStockQuantity(stockQuantity);
		cardItem.setPrice(price);
	    cardItem.setSubCategory(foundSubCategory); 
		cardItem.setImageName(imageName);
		cardItem.setImagePath(imagePath.toString());
		cardItem.setProductLaunchDate(localDate);
		selectedItemRepository.save(cardItem);
	}
	

	// Update Existing CardItem By Admin
	public void updateCardItem(Integer id, String title, String description, Integer stockQuantity, Double price,
			Integer categoryId,
			Integer subCategoryId,String productLaunchDate,MultipartFile image) throws IOException {
		Optional<CardItem> cardItem=selectedItemRepository.findById(id);
        SubCategory foundSubCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.parse(productLaunchDate, formatter);
		Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); 
        }
        String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = uploadPath.resolve(imageName); 
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
		if(cardItem.isPresent()) {
			CardItem exitsItem = cardItem.get();
			exitsItem.setTitle(title);
			exitsItem.setDescription(description);
			exitsItem.setPrice(price);
			exitsItem.setSubCategory(foundSubCategory); 
			exitsItem.setStockQuantity(stockQuantity);
			exitsItem.setProductLaunchDate(localDate);
			if(image!=null && !image.isEmpty()) {
				
				exitsItem.setImageName(imageName);
				exitsItem.setImagePath(imagePath.toString());
			}
			
			selectedItemRepository.save(exitsItem);	
		}
		
	}
		
	
}
