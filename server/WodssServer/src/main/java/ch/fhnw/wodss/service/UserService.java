package ch.fhnw.wodss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.repository.UserRepository;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User saveUser(User user){
		return userRepository.save(user);
	}
	
	public void deleteUser(User user){
		userRepository.delete(user);
	}
}
