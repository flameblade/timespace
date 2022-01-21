package com.spacetime.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spacetime.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	public User findByUsername(String username);
	public User findById(long id);
	public User findByRefreshToken(String refreshToken);
	
	

}
