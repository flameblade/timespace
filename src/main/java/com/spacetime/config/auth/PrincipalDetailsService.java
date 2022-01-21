package com.spacetime.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spacetime.model.User;
import com.spacetime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		System.out.println("PrincipalDetailsService의 loadUserByUsername()" + new PrincipalDetails(user));

		// session.setAttribute("loginUser", user);
		return new PrincipalDetails(user);
	}

	public UserDetails loadUserById(long id) throws UsernameNotFoundException {
		User user = userRepository.findById(id);
		if ("id".equals(id)) {
			return new PrincipalDetails(user);
		} else {

			throw new UsernameNotFoundException("User not found with ID : " + id);
		}
	}

	public UserDetails loadUserByRefreshToken(String refreshToken) throws UsernameNotFoundException {
		User user = userRepository.findByRefreshToken(refreshToken);
		if ("refreshToken".equals(refreshToken)) {
			return new PrincipalDetails(user);
		} else {

			throw new UsernameNotFoundException("User not found with ID : " + refreshToken);
		}
		// session.setAttribute("loginUser", user);
	}
	
	
}
