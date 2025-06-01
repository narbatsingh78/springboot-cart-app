package com.springbootcartapp.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.CustomUserDetails;
import com.springbootcartapp.repository.SelectedItemRepository;
import com.springbootcartapp.services.CardItemService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MyController {

	@Autowired
	private SelectedItemRepository selectedItemRepository;
	
	@Autowired
	public CardItemService cardItemService;
	
	private static final String IMAGE_DIRECTORY = "C:/Users/Narbat Singh/UploadImages/";

	@GetMapping("/items")
	public ResponseEntity<Page<CardItem>> getAllItems(@AuthenticationPrincipal  CustomUserDetails customUserDetails,@RequestParam int pageNumber,@RequestParam int pageSize) {
		  // Extract roles as String
	    String role = customUserDetails.getAuthorities()
	                    .stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .findFirst()
	                    .orElse("ROLE_USER"); // fallback in case none found

	    System.out.println("Role..." + role);
		if (pageNumber < 0 || pageSize <= 0) {
	        return ResponseEntity.badRequest().body(null);
	    }
		
	    Page<CardItem>cardItem  = cardItemService.getAllItems(role,pageNumber, pageSize);
	    return ResponseEntity.ok(cardItem);
	}
	 
	@GetMapping("/items/getByCategoryId")
    public ResponseEntity<Page<CardItem>> getCardItemsByCategoryId(@AuthenticationPrincipal  CustomUserDetails customUserDetails,@RequestParam Integer categoryId,@RequestParam Integer pageNumber,@RequestParam Integer pageSize) {
		String role = customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
		Page<CardItem> cardItemByCategoryId= cardItemService.getCardByCategoryId(role,categoryId, pageNumber, pageSize);
        
        return ResponseEntity.ok(cardItemByCategoryId);
    }
	
	
	 @GetMapping("/items/getBySubCategoryId")
	    public ResponseEntity<Page<CardItem>> getCardItemsBySubCategoryId(@AuthenticationPrincipal  CustomUserDetails customUserDetails,@RequestParam Integer subCategoryId,@RequestParam Integer pageNumber,@RequestParam Integer pageSize) {
		 String role = customUserDetails.getAuthorities()
                 .stream()
                 .map(GrantedAuthority::getAuthority)
                 .findFirst()
                 .orElse("ROLE_USER");
		 Page<CardItem> cardItemBySubCategoryId= cardItemService.getCardBySubCategoryId(role,subCategoryId, pageNumber, pageSize);
	        
	        return ResponseEntity.ok(cardItemBySubCategoryId);
	    }
	 
	@GetMapping("/items/searchItemByTitle")
	public ResponseEntity<Page<CardItem>> getCardItemsByTitleSearch(@AuthenticationPrincipal  CustomUserDetails customUserDetails,@RequestParam String keyword,Integer pageNumber,@RequestParam Integer pageSize ){
		String role = customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
		Page<CardItem>cardItemBySearch=cardItemService.getCardItemsByTitleSearch(role,keyword,pageNumber,pageSize);
		return ResponseEntity.ok(cardItemBySearch);
	}
	
	@PostMapping("/items/addDataInTable")
	public ResponseEntity<String> addCardItem(@RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("stockQuantity") Integer stockQuantity,
			@RequestParam("price") Double price, @RequestParam("category") Integer categoryId,
			@RequestParam("subCategory") Integer subCategoryId,@RequestParam("productLaunchDate") String productLaunchDate,
			@RequestParam("image") MultipartFile image,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
		cardItemService.addCardItem(title, description, stockQuantity, price, categoryId,subCategoryId,productLaunchDate ,image,customUserDetails);
		return ResponseEntity.ok("Record Inserted");
	}

	@PutMapping("/items/update/{id}")
	public ResponseEntity<String> updateCardItem(@PathVariable int id, @RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("stockQuantity") Integer stockQuantity,
			@RequestParam("price") Double price, @RequestParam("category") Integer categoryId,
			@RequestParam("subCategory") Integer subCategoryId,
			@RequestParam("productLaunchDate") String productLaunchDate,
			@RequestParam("image") MultipartFile image) throws IOException {
		cardItemService.updateCardItem(id,title, description, stockQuantity, price, categoryId,subCategoryId,productLaunchDate, image);
		
		return ResponseEntity.ok("Data Update");

	}

	@DeleteMapping("/items/delete/{id}")
	public ResponseEntity<String> updateCardItem(@PathVariable int id) throws IOException {
		Optional<CardItem> cardItem = selectedItemRepository.findById(id);
		if (cardItem.isPresent()) {

			selectedItemRepository.deleteById(id);
		}
		return ResponseEntity.ok("Data Deleted");

	}

	@PatchMapping("/items/updateStockQuantity/{id}")
	public ResponseEntity<String> updateStock(@PathVariable Integer id, @RequestParam Integer stockQuantity,
			@RequestParam Integer priority) {

		CardItem cardItem = selectedItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product Not Found"));

		cardItem.setStockQuantity(stockQuantity);
		cardItem.setPriority(priority);
		selectedItemRepository.save(cardItem);

		return ResponseEntity.ok("Record Insert");
	}

	 @GetMapping("/api/images/{imageName}")
	    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
	        try {
	            Path imagePath = Paths.get(IMAGE_DIRECTORY).resolve(imageName).normalize();
	            Resource resource = new UrlResource(imagePath.toUri());

	            if (!resource.exists() || !resource.isReadable()) {
	                return ResponseEntity.notFound().build();
	            }

	            MediaType mediaType = getMediaType(imageName);

	            return ResponseEntity.ok()
	                    .contentType(mediaType)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
	                    .body(resource);

	        } catch (MalformedURLException e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }

	    private MediaType getMediaType(String fileName) {
	        String name = fileName.toLowerCase();
	        if (name.endsWith(".png")) return MediaType.IMAGE_PNG;
	        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
	        if (name.endsWith(".gif")) return MediaType.IMAGE_GIF;
	        return MediaType.APPLICATION_OCTET_STREAM;
	    }
}