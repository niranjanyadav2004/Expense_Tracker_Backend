package com.niranjan.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.niranjan.DTO.SignUpRequest;
import com.niranjan.DTO.UserResponse;
import com.niranjan.customExceptions.UserAlreadyExistException;
import com.niranjan.customExceptions.UserNotFoundException;
import com.niranjan.entities.User;
import com.niranjan.enums.Roles;
import com.niranjan.repos.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${app.url}")
	private String url;

	@Autowired
	private EmailService emailService;
	
	@PostConstruct
	public void creatAdminAccount() {
		 List<User> list = userRepository.findByRole(Roles.ADMIN);
		if(list==null) {
		   User user = new User();
		   user.setUserID(UUID.randomUUID().toString());
		   user.setName("admin");
		   user.setEmail("admin@gmail.com");
		   user.setPassword(passwordEncoder.encode("admin"));
		   user.setAbout("I am an admin..!!!");
		   user.setRole(Roles.ADMIN);
		   userRepository.save(user);
		}
	}
	
	public User createUser(SignUpRequest signUpRequest) {
		
		if(userRepository.existsByName(signUpRequest.getName())) {
			throw new UserAlreadyExistException("User already exist with name : " + signUpRequest.getName());
		}
		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new UserAlreadyExistException("User already exist with email : " + signUpRequest.getEmail());
		}
		
		User user = new User();
		
		user.setUserID(UUID.randomUUID().toString());
		user.setName(signUpRequest.getName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		user.setAbout(signUpRequest.getAbout());
		user.setRole(Roles.USER);
		
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers(){
		return userRepository.findByRole(Roles.USER);
	}
	
	public UserResponse getUserByUsername(String username) {
		User existUser = userRepository.findByName(username).orElseThrow(()-> new UserNotFoundException("User not found with username : " + username));
		return new UserResponse(existUser.getUserID(), existUser.getName(), existUser.getEmail());
	}

	public void createAndSendAccessToken(String email){
		User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("email is not registerd with mail " + email));

		String accessToken = UUID.randomUUID().toString();
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

		user.setAccessToken(accessToken);
		user.setExpiry(expiry);

		userRepository.save(user);

		String link = url + "/reset-password?token="+accessToken;

		emailService.sendMail(email, link);
	}

	public void resetPassword(String token, String password){
		if(token==null || token.isBlank()) throw new RuntimeException("Token is required");

		User user = userRepository.findByAccessToken(token)
				.orElseThrow(()->new UserNotFoundException("Invalid token"));

		if(user.getExpiry() == null || user.getExpiry().isBefore(LocalDateTime.now()))
				throw new RuntimeException("Token expired. Please request again");

		user.setPassword(passwordEncoder.encode(password));
		user.setAccessToken(null);
		user.setExpiry(null);


		userRepository.save(user);
	}
	
}

