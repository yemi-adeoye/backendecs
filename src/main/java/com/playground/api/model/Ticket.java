package com.playground.api.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.playground.api.enums.PriorityEnum;
import com.playground.api.enums.TicketStatus;

@Entity
public class Ticket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	@Column(length = 3000)
	private String issue; 
	
	@Enumerated(EnumType.STRING)
	private PriorityEnum priority;
	
	private LocalDate genaratedDate;
	
	@Enumerated(EnumType.STRING)
	private TicketStatus status; //OPEN CLOSED
	
	@Column(length = 2000)
	private String response;
	
	@OneToOne
	private Employee employee;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public PriorityEnum getPriority() {
		return priority;
	}

	public void setPriority(PriorityEnum priority) {
		this.priority = priority;
	}

	public LocalDate getGenaratedDate() {
		return genaratedDate;
	}

	public void setGenaratedDate(LocalDate genaratedDate) {
		this.genaratedDate = genaratedDate;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", issue=" + issue + ", priority=" + priority + ", genaratedDate=" + genaratedDate
				+ ", status=" + status + ", response=" + response + ", employee=" + employee + "]";
	}
	
	
}
