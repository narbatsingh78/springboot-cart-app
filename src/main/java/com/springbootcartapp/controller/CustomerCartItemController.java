package com.springbootcartapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springbootcartapp.Dto.RequestDto;
import com.springbootcartapp.Dto.ResponseDto;
import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.CustomUserDetails;
import com.springbootcartapp.entities.CustomerCartItems;
import com.springbootcartapp.entities.User;
import com.springbootcartapp.repository.AddInCartItemRepository;
import com.springbootcartapp.repository.SelectedItemRepository;
import com.springbootcartapp.repository.UserRepository;
import com.springbootcartapp.security.JwtAuthenticationFilter;
import com.springbootcartapp.security.JwtUtil;
import com.springbootcartapp.services.CardItemService;
import com.springbootcartapp.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerCartItemController {

	@Autowired
	public SelectedItemRepository selectedItemRepository;

	@Autowired
	public AddInCartItemRepository addInCartItemRepository;

	@Autowired
	public CardItemService cardItemService;

	@Autowired
	public UserService userService;

	@Autowired
	public JwtUtil jwtUtil;

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/items/cartData")
	public ResponseEntity<List<CustomerCartItems>> getAllCartItems(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		List<CustomerCartItems> cartResponseDto = cardItemService.getAllCartItemsByUser(customUserDetails);
		return ResponseEntity.ok(cartResponseDto);
	}

	@PostMapping("/items/addQuantityInCustomerTable/{id}")
	public ResponseEntity<String> addCardItem(@PathVariable int id, @RequestParam Integer quantity,@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
		
		int userId = customUserDetails.getUserId();
		Optional<CardItem> cardItemOpt = selectedItemRepository.findById(id);

		if (cardItemOpt.isPresent()) {
			CardItem cardItem = cardItemOpt.get();
			System.out.println("Cart Item..."+cardItem);

			Optional<User> userOpt = userRepository.findById(userId);

			if (!userOpt.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
			}
			User user = userOpt.get();

			Optional<CustomerCartItems> existingCartItemOpt = addInCartItemRepository.findByUserAndCardItem(user,
					cardItem);

			if (existingCartItemOpt.isPresent()) {
				CustomerCartItems existingCartItem = existingCartItemOpt.get();
				existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
				addInCartItemRepository.save(existingCartItem);
				return ResponseEntity.ok("Quantity updated successfully");
			} else {

				CustomerCartItems newCartItem = new CustomerCartItems();
				newCartItem.setCardItem(cardItem);
				newCartItem.setQuantity(quantity);
				newCartItem.setUser(user);
				addInCartItemRepository.save(newCartItem);
				return ResponseEntity.ok("Item added to cart");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CardItem not found with ID: " + id);
		}
	}

	@PatchMapping("/items/updateQuantityInCutomerTable/{id}")
	public ResponseEntity<String> updateQuantity(@PathVariable Integer id, @RequestParam Integer quantity) {

		CustomerCartItems customerCartItems = addInCartItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product Not Found"));
		customerCartItems.setQuantity(quantity);
		addInCartItemRepository.save(customerCartItems);

		return ResponseEntity.ok("Record Insert");
	}

	@PostMapping("items/purchaseConfirm")
	public ResponseEntity<String> getPurchaseOrder(@RequestBody List<CustomerCartItems> customerCartItems){
		System.out.println("purchase confirm..."+customerCartItems);
		for(CustomerCartItems cartItems:customerCartItems) {
			System.out.println("In Loop...."+cartItems.getCustomer_ItemId());
			System.out.println("In Loop...."+cartItems.getCardItem().getUserRating());
			addInCartItemRepository.deleteById(cartItems.getCustomer_ItemId());
			Optional<CardItem> cardItem=selectedItemRepository.findById(cartItems.getCardItem().getId());
			if(cardItem.isPresent()) {
				CardItem existCardItem=cardItem.get();
				existCardItem.setUserRating(cartItems.getCardItem().getUserRating());
				selectedItemRepository.save(existCardItem);
			}
			
		}
		System.out.println("purchase confirm..."+customerCartItems);
//		addInCartItemRepository.deleteById(customerCartItems.getCustomer_ItemId());
		return ResponseEntity.ok("Success");
	}
	
	@DeleteMapping("/items/delete/cart/{id}")
	public ResponseEntity<String> updateCardItem(@PathVariable int id) throws IOException {
		Optional<CustomerCartItems> customerCartItems = addInCartItemRepository.findById(id);
		if (customerCartItems.isPresent()) {
			addInCartItemRepository.deleteById(id);
		}
		return ResponseEntity.ok("Data Deleted");

	}

}
