package com.playground.api.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.playground.api.dto.AccessDto;
import com.playground.api.dto.EmployeeDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.dto.SignUpDto;
import com.playground.api.model.Admin;
import com.playground.api.model.Employee;
import com.playground.api.model.Manager;
import com.playground.api.model.User;
import com.playground.api.repositories.AdminRepository;
import com.playground.api.repositories.EmployeeRepository;
import com.playground.api.repositories.ManagerRepository;
import com.playground.api.repositories.UserRepository;
import com.playground.api.service.MailService;

@RestController
@RequestMapping("/api/user")
 public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ResponseDto responseDto; 

	@Autowired
	org.springframework.core.env.Environment env;

	@Autowired
	private ManagerRepository managerRepository; 

	@Autowired
	private AdminRepository adminRepository; 

	@Autowired
	private ManagerController managerController;

	@Autowired
	private EmployeeController employeeController;

	@Autowired
	private AdminController adminController;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	MailService mailService;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping("/grant-access/{email}/{accessMessage}")
	public ResponseEntity<ResponseDto> grantAccess(@PathVariable("email") String username, @PathVariable("accessMessage") String accessMessage) {
		/* Fetch User record by username */
		User user = userRepository.findUserByUsername(username);
		
		if(user == null) {
			responseDto.setMsg("User Data invalid");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
		}

		Employee employee = employeeRepository.findByUserId(user.getId());

		String name = employee.getName();

		/* enable the user */
		user.setEnabled(true);
		/* save the user again */
		userRepository.save(user);
		responseDto.setMsg("User Activated");

		// send user mail
		String baseUrl = env.getProperty("base_url");
		String link = baseUrl + "login/";
		String fromEmail = env.getProperty("from_email");

		String body = "<strong style='font-size:1.4em;'>Dear " + name + ",</strong>";
		body += "<h2 style='margin-bottom: 0;'>Access Granted</h2><hr/>";
		body += "<p style='font-size:1.4em; line-height: 2.5em;'>You've been granted access<br />";
		if (accessMessage != null && accessMessage.length() != 0){
			body += "<b>Access Grant Message: " + accessMessage;
		}
		body += "</b><br />To login, click <a href="
				+ link + "> here </a>, or copy the url below into your browser's address bar.<br/>";
		body += "<code>" + link + "</code></p>";

		// send mail to the user granted access
		try {
			mailService.sendHtmlMessage(fromEmail, user.getUsername(), "ACCESS GRANTED", body);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);

	}

	@PostMapping("/signup")
	public ResponseEntity<ResponseDto> addUser(@RequestBody SignUpDto dto){
		
		/* Employee email is not already present */
		User user = userRepository.findUserByUsername(dto.getEmail());
		
		responseDto.setMsg("Employee Already Exists");
		
		if(! (user == null) )
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(responseDto);

		// first add the user
		user = new User(); //POJO
		user.setUsername(dto.getEmail());
		user.setPassword(encoder.encode(dto.getPassword()));		
		user.setEnabled(false);
		user.setRole(dto.getRole());

		

		String role = dto.getRole();
		

		if(role.equals("EMPLOYEE")){
			// GET EMPLOYEE MANAGER
			// SAVE EMPLOYEE 
			// ADD TO USER AND SAVE
			
			Manager manager = managerRepository.getByEmail(dto.getManagerEmail());
			
			if (Objects.isNull(manager)){
				responseDto.setMsg("Manager does not exist");
				return ResponseEntity.status(HttpStatus.OK).body(responseDto);
			}

			user = userRepository.save(user); //save user

			return employeeController.addEmployee(dto, manager, user);
			
		}

		if(role.equals("MANAGER")){
			// GET MANAGER ADMIN
			// ADD TO USER AND SAVE
			Admin admin = adminRepository.findByName(dto.getAdminName());

			if (Objects.isNull(admin)){
				responseDto.setMsg("Admin does not exist");
				return ResponseEntity.status(HttpStatus.OK).body(responseDto);
			}

			// save user
			user = userRepository.save(user);
			return managerController.addManager(dto, user, admin);
		}

		if(role.equals("ADMIN")){
			// GET ROOT ADMIN
			// ADD TO USER AND SAVE
			user = userRepository.save(user);
			return adminController.add(dto, user);
		}
		//responseDto.setMsg("Manager added to the DB");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@PutMapping("/revoke-access/{email}")
	public ResponseEntity<ResponseDto> revokeAdminAccess(@PathVariable String email,
		 @RequestBody AccessDto accessDto){
		User user = userRepository.findUserByUsername(email);
		user.setEnabled(false);
		userRepository.save(user);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setData(user);
		responseDto.setMsg("Access revoked successfully");

		// send mail to user letting him know his access has been revoked
		String baseUrl = env.getProperty("base_url");
		String link = baseUrl + "login/";
		String fromEmail = env.getProperty("from_email");
		
		String accessMessage = accessDto.getAccessMessage();
		

		String body = "<strong style='font-size:1.4em;'>Dear user,</strong>";
		body += "<h2 style='margin-bottom: 0;'>Access Revoked</h2><hr/>";
		body += "<p style='font-size:1.4em; line-height: 2.5em;'>Your access has been revoked<br />";
		if (accessMessage != null && accessMessage.length() != 0){
			body += "<b>Access Revoke Message: " + accessMessage;
		}
		body += "</b></p>";

		// send mail to the user granted access
		try {
			mailService.sendHtmlMessage(fromEmail, user.getUsername(), "ACCESS GRANTED", body);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
}
