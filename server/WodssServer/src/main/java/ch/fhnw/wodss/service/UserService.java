package ch.fhnw.wodss.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.fhnw.wodss.domain.User;
import ch.fhnw.wodss.repository.UserRepository;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserService(){
		super();
	}

	@Transactional
	public User saveUser(User user){
		return userRepository.save(user);
	}
	
	@Transactional
	public void deleteUser(User user){
		userRepository.delete(user);
	}
	
	@Transactional
	public void deleteUser(Integer id){
		userRepository.delete(id);
	}
	
	public List<User> getAll(){
		return userRepository.findAll();
	}
	
	public User getById(Integer id){
		return userRepository.findOne(id);
	}
	
	public User getByEmail(String email){
		return userRepository.findByEmail(email);
	}
}
