package com.playground.api.dto;

public class PasswordRequestDto {
	private String password;
	private String rand;
	public PasswordRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PasswordRequestDto(String password, String rand) {
		super();
		this.password = password;
		this.rand = rand;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRand() {
		return rand;
	}
	public void setRand(String rand) {
		this.rand = rand;
	}

	
	
	
}
