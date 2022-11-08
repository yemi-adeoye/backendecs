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
import javax.persistence.Table;

import com.playground.api.enums.LeaveEnum;
import com.playground.api.enums.RecordStatus;

@Entity
@Table(name = "leave_details")
public class Leave {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "to_date")
	private LocalDate to;
	@Column(name = "from_date")
	private LocalDate from;
	private Integer days;
	@Enumerated(EnumType.STRING)
	private LeaveEnum status;  //Enumerated types: Enums  : PENDING, APPROVED, DENIED
	@Column(length = 1000)
	private String response; 
	
	@OneToOne
	private Employee employee;
	
	@Enumerated(EnumType.STRING)
	private RecordStatus recordStatus; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	
	public LeaveEnum getStatus() {
		return status;
	}

	public void setStatus(LeaveEnum status) {
		this.status = status;
	}

	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	
	public RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	@Override
	public String toString() {
		return "Leave [id=" + id + ", to=" + to + ", from=" + from + ", days=" + days + ", employee=" + employee + "]";
	}

	
}
