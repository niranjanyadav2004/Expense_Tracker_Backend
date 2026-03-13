package com.niranjan.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niranjan.entities.User;
import com.niranjan.enums.Roles;


public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findByEmail(String email);
	
	List<User> findByRole(Roles role);
	
	Optional<User> findByName(String name);
	
	boolean existsByName(String username);
	boolean existsByEmail(String email);

    Optional<User> findByAccessToken(String accessToken);

}
