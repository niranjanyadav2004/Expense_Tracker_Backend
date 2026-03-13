package com.niranjan.controller;

import com.niranjan.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.niranjan.entities.RefreshToken;
import com.niranjan.entities.User;
import com.niranjan.security.JwtHelper;
import com.niranjan.serviceImpl.RefreshTokenService;
import com.niranjan.serviceImpl.UserService;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
    private UserDetailsService userDetailsService;

	@Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager manager;
    
    @Autowired
    private UserService userService;


    @Autowired
    private JwtHelper helper;
    
    
    @PostMapping("/register")
    public User createUser(@RequestBody SignUpRequest signUpRequest) {
    	return userService.createUser(signUpRequest);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        
        
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());
        User user = refreshToken.getUser();
        

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken.getRefresToken())
                .username(userDetails.getUsername())
                .role(user.getRole().toString())
                .name(user.getName())
                .about(user.getAbout())
                .build();
   
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/refresh")
    public JwtResponse refreshJwtToken(@RequestBody RefreshTokenRequest request){
    	  RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
    	  User user = refreshToken.getUser();
    	  String token = helper.generateToken(user);
    	  
    	  return JwtResponse.builder()
    			            .jwtToken(token)
    			            .refreshToken(refreshToken.getRefresToken())
    			            .username(user.getUsername())
    			            .role(user.getRole().toString())
    			            .name(user.getName())
    			            .about(user.getAbout())
    			            .build();
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO){
        userService.createAndSendAccessToken(forgetPasswordDTO.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Reset password link sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, @RequestParam String token){
        userService.resetPassword(token ,resetPasswordDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully. Please Login");
    }
    

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

   
    
	
}
