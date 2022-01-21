package com.spacetime.dto;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

import com.spacetime.model.Project;
import com.spacetime.model.User;

import lombok.Getter;

@Getter
public class ProjectResponseDto {

	private Long id;
	private String title;
	private Blob content;
	private LocalDateTime startTime;
	private LocalDateTime expireTime;
	private LocalDateTime uploadTime;
	private LocalDateTime updateTime;
	private User prWriter;
	private User prMember;
	 private String permission;
	 
	 
	public ProjectResponseDto(Project entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.startTime = entity.getStartTime();
		this.expireTime = entity.getExpireTime();
		this.uploadTime = entity.getUploadTime();
		this.updateTime = entity.getUpdateTime();
		this.prWriter = entity.getPrWriter();
		this.prMember = entity.getPrMember();
		this.permission = entity.getPermission();
	}
	 
	 
}
