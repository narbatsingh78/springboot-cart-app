package com.springbootcartapp.services;

import com.springbootcartapp.entities.User;
import com.springbootcartapp.repository.UserRepository;
import com.springbootcartapp.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getUserById(Integer id) {
		return userRepository.findById(id).orElse(null);
	}

	public String verifyUserToGenerateToken(User user) {
		String userEmail = user.getUserEmail();
		String password = user.getPassword();
		Optional<User> user1 = userRepository.findByUserEmail(userEmail);
 
		if(user1.isPresent() && passwordEncoder.matches(password, user1.get().getPassword()))
		{
			return jwtUtil.generateToken(userEmail);
//			return token;
		}else if (user1.isPresent() && user1.get().getPassword().equals(password)){
			return jwtUtil.generateToken(userEmail);
		}
		return null;
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByUserEmail(email);
	}

	public String newUserRegister(User user) {

		Optional<User> existingUser = userRepository.findByUserEmail(user.getUserEmail());
		String encodedPassword =passwordEncoder.encode(user.getPassword());
		System.out.println("encodedPassword"+encodedPassword);

		if (existingUser.isPresent()) {
			return "Email already exists.";
		}else {
			user.setPassword(encodedPassword);
//			user.setRole("USER");
			userRepository.save(user);
			return "User registered successfully!";
		}

	}

}
