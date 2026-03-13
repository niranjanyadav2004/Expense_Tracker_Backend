package com.niranjan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niranjan.serviceImpl.UserService;


@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private UserService service;
	
    
    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
    	return ResponseEntity.status(HttpStatus.OK).body(service.getUserByUsername(username));
    }
	
}

