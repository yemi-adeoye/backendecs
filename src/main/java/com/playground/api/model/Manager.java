package com.playground.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity 
public class Manager {  

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; 
	
	private String name;
	private String imageUrl;
	private String jobTitle; 
	
	@OneToOne
	private User user;

	@OneToOne
	private Admin admin;
 
	

	public Manager(long id, String name, String imageUrl, String jobTitle, User user, Admin admin) {
		super();
		this.name = name;
		this.imageUrl = imageUrl;
		this.jobTitle = jobTitle;
		this.user = user;
		this.admin = admin;
	}
	

	public Manager() {
		super();
		
	}

	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", name='" + getName() + "'" +
			", imageUrl='" + getImageUrl() + "'" +
			", jobTitle='" + getJobTitle() + "'" +
			", user='" + getUser() + "'" +
			", admin='" + getAdmin() + "'" +
			"}";
	}

	
	
}
