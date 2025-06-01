package com.springbootcartapp.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springbootcartapp.Dto.LoginResponseDto;
import com.springbootcartapp.entities.User;
import com.springbootcartapp.security.JwtUtil;
import com.springbootcartapp.services.UserService;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	JwtUtil jwtUtil;
	@Configuration
	public class WebConfig implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("http://localhost:3000")
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
					.allowCredentials(true);
		}
	}

	@PostMapping("/api/login")
	public ResponseEntity<?> verifyUser(@RequestBody User user) {
		
		String token = userService.verifyUserToGenerateToken(user);
		if (token != null) {
			Optional<User> authenticatedUser = userService.getUserByEmail(user.getUserEmail());

			if (authenticatedUser.isPresent()) {
				User userData = authenticatedUser.get();
				LoginResponseDto response = new LoginResponseDto(token, userData);
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
			}
		} else {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}
	

	@PostMapping("/api/register")
	public ResponseEntity<String> userRegister(@RequestBody User user) {
		String registrationMessage = userService.newUserRegister(user);

		if (registrationMessage.equals("User registered successfully!")) {
			return new ResponseEntity<>(registrationMessage, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(registrationMessage, HttpStatus.BAD_REQUEST);
		}
	}
	
	  @PostMapping("items/api/refreshToken")
	    public ResponseEntity<?>  refreshToken(@RequestHeader("Authorization") String token) {
	        String bearerToken=token.substring(7);
	        System.out.println("bearerToken"+bearerToken);
	        String username = jwtUtil.extractUsername(bearerToken);
	        if (jwtUtil.validateToken(bearerToken,username)) {
	            String newRefreshToken = jwtUtil.generateRefreshToken(username);
	            return ResponseEntity.ok(newRefreshToken);
	        } else {
	        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
	            //return throw new RuntimeException("Invalid refresh token");
	        }
	    }
	
	
	
}
