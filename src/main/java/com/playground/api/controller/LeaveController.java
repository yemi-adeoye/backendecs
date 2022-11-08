package com.playground.api.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.api.dto.LeaveDto;
import com.playground.api.dto.ReqLeaveDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.enums.LeaveEnum;
import com.playground.api.enums.RecordStatus;
import com.playground.api.model.Employee;
import com.playground.api.model.Leave;
import com.playground.api.repositories.EmployeeRepository;
import com.playground.api.repositories.LeaveRepository;

@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = {"http://localhost:4200"})
public class LeaveController {
	
	@Autowired
	private LeaveRepository leaveRepository; 
	@Autowired
	private EmployeeRepository employeeRepository; 
	@Autowired
	private ResponseDto responseDto; 
	@PostMapping("/add")
	public Leave addLeave(@RequestBody LeaveDto leaveDto, Principal principal) {
		String username = principal.getName();
		Employee employee = employeeRepository.getByEmail(username);
		
		Leave leave = new Leave();
		leave.setFrom(leaveDto.getFrom());
		leave.setTo(leaveDto.getTo());
		leave.setDays(leaveDto.getDays());
		leave.setEmployee(employee);
		leave.setStatus(LeaveEnum.PENDING);
		leave.setRecordStatus(RecordStatus.ACTIVE);
		leave = leaveRepository.save(leave);
		 
		return leave;
	}
	
	@GetMapping("/employee/all/{status}")
	public List<LeaveDto> getAllLeaves(@PathVariable("status") String status, Principal principal) {
		String username = principal.getName();
		Employee employee = employeeRepository.getByEmail(username);
		LeaveEnum statusVal = LeaveEnum.valueOf(status);
		List<Leave> list = 
				leaveRepository.getLeavesByEmployeeUsername(employee.getUser().getUsername(),statusVal, RecordStatus.ACTIVE);
		List<LeaveDto> listDto = LeaveDto.convertToDto(list);
		return listDto;
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseDto> deleteLeaveSoft(@PathVariable("id") Long id) {
			/*
			 * TO Hard Delete the Record: 
			 * leaveRepository.deleteById(id);
			 */
			Optional<Leave> optional = leaveRepository.findById(id);
			if(!optional.isPresent()) {
				responseDto.setMsg("Invalid Leave ID");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
			}
			Leave leave = optional.get();
			leave.setRecordStatus(RecordStatus.DELETED);
			leaveRepository.save(leave);
			responseDto.setMsg("Record Archived");
			return ResponseEntity.status(HttpStatus.OK).body(responseDto);
			
	}
	
	@GetMapping("/all")
	public List<LeaveDto> fetchPendingLeavesByManagerEmail(Principal principal) {
		String managerUsername = principal.getName();
		
		/* Fetch Employees by managerUsername */
		List<Employee> list = employeeRepository.getAllEmployeeByManager(managerUsername);
		List<Leave> finalList = new ArrayList<>();
		/* For every employee, fetch pending leaves */
		for(Employee e : list) {
			List<Leave> listLeave = leaveRepository.getLeavesByEmployeeUsername(e.getUser().getUsername(), LeaveEnum.PENDING, RecordStatus.ACTIVE);
			finalList.addAll(listLeave);
		}
		/* Convert it to dto */
		List<LeaveDto> listDto = LeaveDto.convertToDto(finalList);
		return listDto; 
	}

	@GetMapping("/all-admin")
	public List<LeaveDto> fetchPendingLeaves() {
		
		List<Leave> leaves =  leaveRepository.findByStatusAndRecordStatus(
			LeaveEnum.PENDING, RecordStatus.ACTIVE);
			
		/* Convert it to dto */
		List<LeaveDto> listDto = LeaveDto.convertToDto(leaves);
		
		return listDto; 
	}
	
	@GetMapping("/update-status/{leaveID}/{leaveStatus}")
	public ResponseEntity<ResponseDto> updateLeaveStatus(@PathVariable("leaveID") Long leaveID, 
								  @PathVariable("leaveStatus") String leaveStatus) {
		
		LeaveEnum statusVal = LeaveEnum.valueOf(leaveStatus);
		try {
			leaveRepository.updateStatusByid(leaveID,statusVal);
		}
		catch(Exception e) {
			responseDto.setMsg("Incorrect Data Given");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
		}
		
		if(leaveStatus.equalsIgnoreCase(LeaveEnum.APPROVED.toString()) ) {
			Optional<Leave> optional = leaveRepository.findById(leaveID);
			if(!optional.isPresent()) {
				responseDto.setMsg("Incorrect Data Given");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
			}
			
			Leave leave = optional.get();
			int days = leave.getDays();
			
			Employee employee = leaveRepository.getEmployeeByLeaveId(leaveID);
			employee.setLeavesLeft(employee.getLeavesLeft() - days);
			
			employeeRepository.save(employee);
		}
		
		responseDto.setMsg("Leave " + leaveStatus);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateReponse(@RequestBody ReqLeaveDto dto) {
		/* Fetch leave by id given */
		Optional<Leave>  optional = leaveRepository.findById(dto.getId());
		
		if(!optional.isPresent()) {
			responseDto.setMsg("Leave ID Invalid");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
		}
		
		Leave leave = optional.get();
		
		/* update the response and status */
		leave.setResponse(dto.getResponse());
		leave.setStatus(LeaveEnum.DENIED);
		
		/* Save the leave back in DB */
		leaveRepository.save(leave);
		responseDto.setMsg("Response recorded");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
}











