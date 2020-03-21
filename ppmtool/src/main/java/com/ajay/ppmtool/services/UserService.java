package com.ajay.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajay.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.ajay.ppmtool.model.User;
import com.ajay.ppmtool.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newuser) {
		
	
		String username = newuser.getUsername();
		try {
			
			newuser.setPassword(bCryptPasswordEncoder.encode(newuser.getPassword()));
			// Username has to be unique exception
			
			newuser.setUsername(username);
			
			//Make sure password and confirm password match
			
			// we don't persist or show confirm password field
			newuser.setConfirmPassword("");
			
			return userRepository.save(newuser);
		}catch(Exception e) {
			
			throw new UsernameAlreadyExistsException("Username '"+username+"' already exists");
		}
		
	}
}
