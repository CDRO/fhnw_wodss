package ch.fhnw.wodss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.fhnw.wodss.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	
}
