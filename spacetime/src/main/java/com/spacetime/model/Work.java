package com.spacetime.model;

import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Entity;
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
public class Work {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	
	@Lob
	private Blob content;
	
	private Timestamp startTime;
	private Timestamp expireTime;
	
	@CreationTimestamp
	private Timestamp uploadTime;
	
	@UpdateTimestamp
	private Timestamp updateTime;
	
	
	 @ManyToOne
	 @JoinColumn(name = "Work_Writer")
	 private User workWriter;
	 
	 @ManyToOne
	 @JoinColumn(name = "Work_Member")
	 private User workMember;
	 

}
