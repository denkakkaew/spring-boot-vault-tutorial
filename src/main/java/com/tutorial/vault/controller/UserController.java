package com.tutorial.vault.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.vault.repository.User;
import com.tutorial.vault.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<?> getUsers(){
		return ResponseEntity.ok(userService.getUsers());
	}
	
	@PutMapping("/users")
	public ResponseEntity<?> addUser(@RequestBody User user){
		return ResponseEntity.ok(userService.addUser(user));
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> addUser(@PathVariable("id") Integer id){
		return ResponseEntity.ok(userService.deleteUser(id));
	}
}
