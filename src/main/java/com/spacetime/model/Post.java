package com.spacetime.model;

import java.sql.Blob;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	
	@Lob
	private Blob content;
	
	@CreationTimestamp
	private LocalDateTime wirteTime;
	@UpdateTimestamp
	private LocalDateTime updateTime;
	
	private long views;
	

	
	 @ManyToOne
	 @JoinColumn(name = "userId")
	 private User writer;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "category_id")
	 private Category category;
	

}
