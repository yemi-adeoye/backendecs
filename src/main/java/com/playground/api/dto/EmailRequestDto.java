package com.playground.api.dto;

public class EmailRequestDto {
	private String email;

	public EmailRequestDto(String email) {
		super();
		this.email = email;
	}

	public EmailRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
