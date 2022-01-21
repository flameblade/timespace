package com.spacetime.model;

import java.sql.Blob;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	
	@Lob
	private Blob content;
	
	private LocalDateTime startTime;
	private LocalDateTime expireTime;
	
	@CreationTimestamp
	private LocalDateTime uploadTime;
	
	@UpdateTimestamp
	private LocalDateTime updateTime;
	
	
	 @ManyToOne
	 @JoinColumn(name = "prWriter")
	 private User prWriter;
	 
	 @ManyToOne
	 @JoinColumn(name = "prMember")
	 private User prMember;
	 
	 private String permission;

	@Builder
	public Project(String title, Blob content, LocalDateTime startTime, LocalDateTime expireTime, User prWriter,
			User prMember, String permission) {
		super();
		this.title = title;
		this.content = content;
		this.startTime = startTime;
		this.expireTime = expireTime;
		this.prWriter = prWriter;
		this.prMember = prMember;
		this.permission = permission;
	}
	
	 public void update(String title, Blob content, LocalDateTime expireTime, User prWriter, User prMember ) {
	        this.title = title;
	        this.content = content;
	        this.expireTime = expireTime;
	        this.prWriter = prWriter;
	        this.prMember = prMember;
	    }

	



	

}
