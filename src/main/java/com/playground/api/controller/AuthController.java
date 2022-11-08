package com.playground.api.controller;

import java.security.Principal;
import java.util.Random;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.api.dto.AdminDto;
import com.playground.api.dto.EmailRequestDto;
import com.playground.api.dto.EmployeeDto;
import com.playground.api.dto.ManagerDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.model.Admin;
import com.playground.api.model.Employee;
import com.playground.api.model.Manager;
import com.playground.api.model.PasswordResets;
import com.playground.api.model.User;
import com.playground.api.repositories.AdminRepository;
import com.playground.api.repositories.EmployeeRepository;
import com.playground.api.repositories.ManagerRepository;
import com.playground.api.repositories.PasswordResetsRepository;
import com.playground.api.repositories.UserRepository;
import com.playground.api.service.MailService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private ResponseDto responseDto;

	@Autowired
	private PasswordResetsRepository passwordResetsRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private MailService mailService;

	@Autowired
	org.springframework.core.env.Environment env;

	@GetMapping("/login")
	public ResponseEntity<ResponseDto> login(Principal principal) { // Injecting Principal Interface: DI(Dependency
		String username = principal.getName();

		if (username == null) {
			responseDto.setMsg("User does not exist");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(responseDto);
		}

		User user = userRepository.findUserByUsername(username);
		System.out.println(user);

		if (user == null) {

			responseDto.setMsg("Invalid Credentials");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(responseDto);
		}

		if (!user.isEnabled()) {
			responseDto.setMsg("Employee Not Activated");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(responseDto);
		}

		responseDto.setMsg("Login Success");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseDto);

	}

	@GetMapping("/user")
	public ResponseEntity<Object> getUser(Principal principal) {
		String username = principal.getName();

		User user = userRepository.findUserByUsername(username);

		if (user.getRole().equalsIgnoreCase("EMPLOYEE")) {
			// fetch employee details by username
			Employee employee = employeeRepository.getByEmail(user.getUsername());
			
			EmployeeDto dto = EmployeeDto.convertToSingleDto(employee);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(dto);
		} else if (user.getRole().equalsIgnoreCase("MANAGER")) {
			// fetch manager details by username
			Manager manager = managerRepository.getByEmail(user.getUsername());
			ManagerDto dto = ManagerDto.convertToDto(manager);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(dto);
			
			
		} else {
			// fetch ADMIN details by username
			Admin admin = adminRepository.getByEmail(user.getUsername());
			
			AdminDto dto = AdminDto.convertToSingleDto(admin);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(dto);
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<Object> sendPasswordReset(@RequestBody EmailRequestDto emailRequestDto) {

		String baseUrl = env.getProperty("base_url");
		String from_email = env.getProperty("from_email");

		String email = emailRequestDto.getEmail();

		User user = userRepository.findUserByUsername(email);

		if (user == null) {
			// this user does not exist: send developer response
			responseDto.setMsg("Email Not Registered, please sign up");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
		}

		String name = "";

		String role = user.getRole();

		// get user name to include in mail
		if (role.equals("EMPLOYEE")) {
			name = employeeRepository.getByEmail(user.getUsername()).getName();
		} else { // if (user.getRole() == "MANAGER") {
			name = managerRepository.getByEmail(user.getUsername()).getName();
		}

		String ota = generateUserOneTimeAccessKey();

		// create an entry for the user in the database
		PasswordResets userPasswordReset = new PasswordResets();

		userPasswordReset.setUser(user);
		userPasswordReset.setRand(ota);

		try {
			passwordResetsRepository.save(userPasswordReset);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		String link = baseUrl + "reset-password/" + ota + "/" + email;

		String body = "<strong style='font-size:1.4em;'>Dear " + name + ",</strong>";
		body += "<h2 style='margin-bottom: 0;'>Password Reset</h2><hr/>";
		body += "<p style='font-size:1.4em; line-height: 2.5em;'>We got a request to reset your password. <br />To reset your password, click <a href="
				+ link + "> here </a>, or copy the url below into your browser's address bar.<br/>";
		body += "<code>" + link + "</code></p>";

		// send the user an email
		try {
			mailService.sendHtmlMessage(from_email, email, "Password Reset", body);
		} catch (Exception e) {
			responseDto.setMsg("Could not send email due to server Issue");
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
		}
		responseDto.setMsg("Success");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	private String generateUserOneTimeAccessKey() {
		Random random = new Random();

		Integer oneInt = random.hashCode();

		return oneInt.toString();
	}

	@GetMapping("/confirm-ota/{ota}/{email}")
	public ResponseEntity<ResponseDto> confirmOta(@PathVariable("ota") String ota,
			@PathVariable("email") String email) {
		PasswordResets reset = passwordResetsRepository.getByOtaAndEmail(ota, email);
		if (reset == null) {
			responseDto.setMsg("Email verification Failed.");
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
		}

		responseDto.setMsg("Success");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@PostMapping("/reset-pass")
	public ResponseEntity<ResponseDto> resetPass(@RequestBody User user) {

		User userDB = userRepository.findUserByUsername(user.getUsername());
		if (userDB == null) {
			responseDto.setMsg("Reset Failed");
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
		}
		userDB.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(userDB);

		responseDto.setMsg("Success");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
}
