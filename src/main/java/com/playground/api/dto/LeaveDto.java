package com.playground.api.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.playground.api.model.Leave;

public class LeaveDto {
	private Long id;
	private LocalDate from;
	private LocalDate to;
	private Integer days;
	private String status;
	private String comments;
	private int leavesLeft;
	private String name; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
	public int getLeavesLeft() {
		return leavesLeft;
	}

	public void setLeavesLeft(int leavesLeft) {
		this.leavesLeft = leavesLeft;
	}
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LeaveDto [id=" + id + ", from=" + from + ", to=" + to + ", days=" + days + ", status=" + status
				+ ", comments=" + comments + "]";
	}

	public static List<LeaveDto> convertToDto(List<Leave> list) {
		List<LeaveDto> listDto = new ArrayList<>();
		for(Leave leave : list) {
			LeaveDto dto = new LeaveDto();
			dto.setId(leave.getId());
			dto.setFrom(leave.getFrom());
			dto.setTo(leave.getTo());
			dto.setDays(leave.getDays());
			dto.setStatus(leave.getStatus().toString());
			dto.setComments(leave.getResponse());
			dto.setLeavesLeft(leave.getEmployee().getLeavesLeft());
			dto.setName(leave.getEmployee().getName());
			listDto.add(dto);
		}
		return listDto; 
	}
	
}
