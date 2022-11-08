package com.playground.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.playground.api.model.Employee;

public class SignUpDto {
	private String name;
	private String jobTitle;
	private String managerEmail;
	private String email;
	private String password;
	private Long id; 
	private String role;
	private String imageUrl;
	private int leavesLeft;
	private int totalLeaves;
	private String managerName;
    private String adminName;
	
	
	public SignUpDto() { }


    public SignUpDto(String name, String jobTitle, String managerEmail, String email, String password, Long id, String role, String imageUrl, int leavesLeft, int totalLeaves, String managerName, String adminName) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerEmail = managerEmail;
        this.email = email;
        this.password = password;
        this.role = role;
        this.imageUrl = imageUrl;
        this.leavesLeft = leavesLeft;
        this.totalLeaves = totalLeaves;
        this.managerName = managerName;
        this.adminName = adminName;
    }
	

    public String getAdminName() {
        return this.adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

	public String getName() {
		return name;
	}

    public String getImageUrl() {
		return imageUrl;
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

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getLeavesLeft() {
		return leavesLeft;
	}

	public void setLeavesLeft(int leavesLeft) {
		this.leavesLeft = leavesLeft;
	}

	public int getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	
	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}


    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", managerEmail='" + getManagerEmail() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", id='" + getId() + "'" +
            ", role='" + getRole() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", leavesLeft='" + getLeavesLeft() + "'" +
            ", totalLeaves='" + getTotalLeaves() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", adminName='" + getAdminName() + "'" +
            "}";
    }


	/*public static List<EmployeeDto> convertToDto(List<Employee> list) {
		
		List<EmployeeDto> listDto = new ArrayList<>();
		for(Employee e : list) {
			EmployeeDto dto = new EmployeeDto();
			//Conversion
			dto.setId(e.getId());
			dto.setEmail(e.getUser().getUsername());
			dto.setPassword("");
			dto.setJobTitle(e.getJobTitle());
			dto.setManagerEmail(e.getManager().getUser().getUsername());
			dto.setRole(e.getUser().getRole());
			dto.setLeavesLeft(e.getLeavesLeft());
			dto.setTotalLeaves(e.getTotalLeaves());
			dto.setName(e.getName());
			listDto.add(dto);
		}
		return listDto;
	}
	
	public static EmployeeDto convertToSingleDto(Employee e){
		EmployeeDto dto = new EmployeeDto();
		//Conversion
		dto.setId(e.getId());
		dto.setEmail(e.getUser().getUsername());
		dto.setPassword("");
		dto.setJobTitle(e.getJobTitle());
		dto.setManagerEmail(e.getManager().getUser().getUsername());
		dto.setRole(e.getUser().getRole());
		dto.setLeavesLeft(e.getLeavesLeft());
		dto.setTotalLeaves(e.getTotalLeaves());
		dto.setName(e.getName());
		return dto;
	}*/
	
}
