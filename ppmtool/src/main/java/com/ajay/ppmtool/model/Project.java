package com.ajay.ppmtool.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity  // to create a java pojo which will be mapped to table in DB
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // primary key of table and increment strategy simillar to autoIncrement in mysql
	private Long id;
	
	@NotBlank(message="project name is required")
	private String projectName;
	
	@NotBlank(message="project identifier is required")
	@Size(min=4,max=5,message="Please use 4 to 5 characters")
	@Column(updatable=false,unique=true)
	private String projectIdentifier;
	
	@NotBlank(message="project description is required")
	private String description;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date start_date;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date end_date;
	
	@Column(updatable=false)
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date created_at;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date updated_at;
	
	// oneToone relationship with backlog
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL,mappedBy="project")
	@JsonIgnore
	private BacklogProject backlog;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private User user;
	
	private String projectLeader;
	
	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public String getProjectLeader() {
		return projectLeader;
	}



	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}



	public Project() {
		
	}
	
	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public String getProjectIdentifier() {
		return projectIdentifier;
	}



	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Date getStart_date() {
		return start_date;
	}



	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}



	public Date getEnd_date() {
		return end_date;
	}



	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}



	public Date getCreated_at() {
		return created_at;
	}



	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}



	public Date getUpdated_at() {
		return updated_at;
	}



	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	
	
	public BacklogProject getBacklog() {
		return backlog;
	}



	public void setBacklog(BacklogProject backlog) {
		this.backlog = backlog;
	}



	@PrePersist    // populate values before persisting the object. done by string internally
	protected void onCreate() {
		this.created_at = new Date();
	}
	
	@PreUpdate     // same as above but just here update instead of create
	protected void onUpdate() {
		this.updated_at = new Date();
	}

}
