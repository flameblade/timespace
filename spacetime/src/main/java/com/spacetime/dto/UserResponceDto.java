package com.spacetime.dto;

import java.time.LocalDateTime;

import com.spacetime.model.User;

import lombok.Getter;

@Getter
public class UserResponceDto {
	private String username;
	private String nickname;
	private long revisit;
	private String status;
	private String roles;
	private String refreshToken;
	private LocalDateTime createdTime;
	private LocalDateTime updateTime;
	
	public UserResponceDto(User entity) {
		this.username = entity.getUsername();
		this.nickname = entity.getNickname();
		this.revisit = entity.getRevisit();
		this.status = entity.getStatus();
		this.roles = entity.getRoles();
		this.createdTime = getCreatedTime();
		this.updateTime = getUpdateTime();
		this.refreshToken = entity.getRefreshToken();
	}
	
	
	
	
}
