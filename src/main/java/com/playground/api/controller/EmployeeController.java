package com.playground.api.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.api.dto.EmployeeDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.dto.SignUpDto;
import com.playground.api.model.Employee;
import com.playground.api.model.Manager;
import com.playground.api.model.User;
import com.playground.api.repositories.EmployeeRepository;
import com.playground.api.repositories.ManagerRepository;
import com.playground.api.repositories.UserRepository;
import com.playground.api.service.MailService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
	ManagerRepository managerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private MailService mailService;

	@Autowired
	private ResponseDto responseDto;

	/*
	 * @PostMapping("/add")
	 * public ResponseEntity<ResponseDto> addEmployee(@RequestBody EmployeeDto dto)
	 * {
	 * 
	 * 
	 * Manager manager = managerRepository.getByEmail(dto.getManagerEmail());
	 * responseDto.setMsg("Manager Email Invalid");
	 * if(manager == null)
	 * return ResponseEntity
	 * .status(HttpStatus.BAD_REQUEST)
	 * .body(responseDto);
	 * 
	 * 
	 * Employee employee = employeeRepository.getByEmail(dto.getEmail());
	 * responseDto.setMsg("Employee Already Exists");
	 * if(! (employee == null) )
	 * return ResponseEntity
	 * .status(HttpStatus.BAD_REQUEST)
	 * .body(responseDto);
	 * 
	 * 
	 * String encryptedPassword =encoder.encode(dto.getPassword());
	 * 
	 * 
	 * User user = new User();
	 * user.setUsername(dto.getEmail());
	 * user.setPassword(encryptedPassword);
	 * user.setRole("EMPLOYEE");
	 * user.setEnabled(false);
	 * user = userRepository.save(user);
	 * 
	 * employee = new Employee();
	 * employee.setUser(user);
	 * employee.setName(dto.getName());
	 * employee.setJobTitle(dto.getJobTitle());
	 * employee.setCreatedOn(LocalDate.now());
	 * employee.setManager(manager);
	 * employeeRepository.save(employee);
	 * 
	 * responseDto.setMsg("Employee Record Added");
	 * 
	 * return ResponseEntity
	 * .status(HttpStatus.OK)
	 * .body(responseDto);
	 * }
	 */

	@PostMapping("/add")
	public ResponseEntity<ResponseDto> addEmployee(@RequestBody SignUpDto dto, Manager manager, User user) {

		Employee employee = new Employee();
		employee.setUser(user);
		employee.setName(dto.getName());
		employee.setJobTitle(dto.getJobTitle());
		employee.setCreatedOn(LocalDate.now());
		employee.setManager(manager);
		try {
			// SEND SIGNUP SUCCESS MAIL
		this.mailService.sendSignUpMail(employee.getName(), user.getUsername());
			employeeRepository.save(employee);
		} catch (Exception e) {
			responseDto.setMsg("Something went wrong");
			return ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(responseDto);
		}
		

		responseDto.setMsg("Employee Record Added");

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseDto);
	}

	@GetMapping("/all")
	public List<EmployeeDto> getAllEmployeeByManagerEmail(Principal principal) {
		String managerEmail = principal.getName();

		List<Employee> list;

		list = employeeRepository.getAllEmployeeByManager(managerEmail);

		List<EmployeeDto> listDto = EmployeeDto.convertToDto(list);

		return listDto;

	}
	
	@GetMapping("/all-admin")
	public List<EmployeeDto> getAllEmployees() {

		List<Employee> list = employeeRepository.findAll();
		
		List<EmployeeDto> listDto = EmployeeDto.convertToDto(list);

		return listDto;

	}

	@GetMapping("/access")
	public List<EmployeeDto> getEmployeeHavingAccess(Principal principal) {
		String username = principal.getName();
		List<Employee> list = employeeRepository.getEmployeeHavingAccess(username, false);
		List<EmployeeDto> listDto = EmployeeDto.convertToDto(list);
		return listDto;
	}

	@GetMapping("/access-all")
	public List<EmployeeDto> getAllEmployeeByAccess() {
		List<Employee> list = employeeRepository.getAllEmployeeHavingAccess(false);
		List<EmployeeDto> listDto = EmployeeDto.convertToDto(list);
		return listDto;
	}
}
