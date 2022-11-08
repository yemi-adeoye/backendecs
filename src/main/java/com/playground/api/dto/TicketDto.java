package com.playground.api.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.playground.api.model.Ticket;

public class TicketDto {
	private String issue;
	private String priority;
	private Long id; 
	private LocalDate generatedDate; 
	private String Status;
	private String response;
	private String name;
	
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(LocalDate generatedDate) {
		this.generatedDate = generatedDate;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<TicketDto> convertToListDto(List<Ticket> list) {
		List<TicketDto> listDto = new ArrayList<>();
		for(Ticket t : list) {
			TicketDto dto = new TicketDto();
			dto.setId(t.getId());
			dto.setIssue(t.getIssue());
			dto.setPriority(t.getPriority().toString());
			dto.setResponse(t.getResponse());
			dto.setStatus(t.getStatus().toString());
			dto.setGeneratedDate(t.getGenaratedDate());
			dto.setName(t.getEmployee().getName());
			listDto.add(dto);
		}
		return listDto;
	}
}
