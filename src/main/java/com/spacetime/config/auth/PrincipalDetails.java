package com.spacetime.config.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spacetime.dto.ProjectResponseDto;
import com.spacetime.dto.UserResponceDto;
import com.spacetime.model.Project;
import com.spacetime.model.User;
import com.spacetime.repository.UserRepository;

public class PrincipalDetails implements UserDetails {

	private User user;
	private Object roles;

	public PrincipalDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	public String getNickname() {
		return user.getNickname();
	}

	public String getRole() {
		return user.getRoles();
	}

	public String getStatus() {
		return user.getStatus();
	}

	public long getRevisit() {
		return user.getRevisit();
	}
	public long getId() {
		return user.getId();
	}
	public String getRefreshToken() {
		return user.getRefreshToken();
	}

	public LocalDateTime getCreateTime() {
		return user.getCreatedTime();
	}

	public LocalDateTime getUpdateTime() {
		return user.getUpdateTime();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		user.getRoleList().forEach(r -> {
			authorities.add(() -> {
				return r;
			});
		});
		return authorities;
	}

}