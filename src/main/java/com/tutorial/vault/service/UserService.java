package com.tutorial.vault.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorial.vault.repository.User;
import com.tutorial.vault.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}

	public User addUser(User user) {
		return userRepository.save(user);
		
	}

	public Object deleteUser(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isEmpty()) {
			userRepository.delete(user.get());
		}
		return user.get();
	}
}
