package com.playground.api.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity  //<-- this creates the DB table
@Table(name = "employee")  //<--this replaces default table name, Is optional.  
public class Employee { //by default table name will be 'employee'
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; 
	
	@Column(nullable = false)
	private String name;
	private String jobTitle;
	private String imageUrl;
	private Integer leavesLeft=20;
	private Integer totalLeaves=20; 
	private LocalDate createdOn;
	
	@OneToOne
	private User user; 
	
	@OneToOne
	private Manager manager; 
	
	public Employee() {  }

	public Employee(Long id, String name, String jobTitle, String imageUrl, Integer leavesLeft, Integer totalLeaves,
			LocalDate createdOn) {
		super();
		this.id = id;
		this.name = name;
		this.jobTitle = jobTitle;
		this.imageUrl = imageUrl;
		this.leavesLeft = leavesLeft;
		this.totalLeaves = totalLeaves;
		this.createdOn = createdOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getLeavesLeft() {
		return leavesLeft;
	}

	public void setLeavesLeft(Integer leavesLeft) {
		this.leavesLeft = leavesLeft;
	}

	public Integer getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Integer totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	 

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", jobTitle=" + jobTitle + ", imageUrl=" + imageUrl
				+ ", leavesLeft=" + leavesLeft + ", totalLeaves=" + totalLeaves + ", createdOn=" + createdOn + ", user="
				+ user + ", manager=" + manager + "]";
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
	
	
}
/* 
 1. Make all instance variables as private.
 2. provide default and arg-contructor
 3. provide getters and setters methods for instance variable manipulation
 4. [optional]- provide toString() method
 */
/* ctrl + shft + o : auto imports*/
/* byte, short, char */
/* int/Integer , float/Float , long/Long , double/Double */