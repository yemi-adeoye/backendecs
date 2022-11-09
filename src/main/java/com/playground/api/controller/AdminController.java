package com.playground.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.api.dto.AccessDto;
import com.playground.api.dto.AdminDto;
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
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	org.springframework.core.env.Environment env;

	@Autowired
	private ResponseDto responseDto;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@PostMapping("/employee/post")
	public ResponseEntity<ResponseDto> postEmployeeCsvData(@RequestBody EmployeeDto[] dtoArry) {
		System.out.println("api called..");
		for (EmployeeDto dto : dtoArry) {
			Employee employee = employeeRepository.getByEmail(dto.getEmail());
			if (employee == null) {
				// convert into Employee Model
				Employee e = new Employee();
				e.setName(dto.getName());
				e.setJobTitle(dto.getJobTitle());
				e.setTotalLeaves(dto.getTotalLeaves());
				e.setLeavesLeft(dto.getLeavesLeft());

				// fetch Manager by name
				Manager manager = managerRepository.findManagerByName(dto.getManagerName());
				if (manager == null) {
					responseDto.setMsg("Please add manager in records");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(responseDto);
				}

				e.setManager(manager);

				// find User by Username
				User user = userRepository.findUserByUsername(dto.getEmail());
				if (user == null) {
					user = new User();
					user.setUsername(dto.getEmail());
					user.setPassword(passwordEncoder.encode(dto.getPassword()));
					user.setEnabled(false);
					user.setRole("EMPLOYEE");
					userRepository.save(user);
				}

				e.setUser(user);

				employeeRepository.save(e);
			} else {
				employee.setName(dto.getName());
				employee.setJobTitle(dto.getJobTitle());
				employee.setTotalLeaves(dto.getTotalLeaves());
				employee.setLeavesLeft(dto.getLeavesLeft());
				// fetch Manager by name
				Manager manager = managerRepository.findManagerByName(dto.getManagerName());
				if (manager == null) {
					responseDto.setMsg("Please add manager in records");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(responseDto);
				}

				employee.setManager(manager);

				// find User by Username
				User user = userRepository.findUserByUsername(dto.getEmail());
				if (user == null) {
					user = new User();
					user.setUsername(dto.getEmail());
					user.setPassword(passwordEncoder.encode(dto.getPassword()));
					user.setEnabled(false);
					user.setRole("EMPLOYEE");
				}

				employee.setUser(user);

				employeeRepository.save(employee);
			}
		}

		responseDto.setMsg("Operation Successful");
		return ResponseEntity.status(HttpStatus.OK)
				.body(responseDto);

	}

	@PostMapping("/add")
	public ResponseEntity<ResponseDto> add(@RequestBody SignUpDto dto, User user) {

		// Save manager
		Admin admin = new Admin();
		admin.setUser(user);
		admin.setName(dto.getName());
		admin.setImageUrl(dto.getImageUrl());
		admin.setJobTitle(dto.getJobTitle());

		// SEND SIGNUP SUCCESS MAIL
		this.mailService.sendSignUpMail(admin.getName(), user.getUsername());

		adminRepository.save(admin);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setData(admin);
		responseDto.setMsg("admin added to the DB");

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@GetMapping("/all")
	public List<AdminDto> all() {
		List<Admin> admins = adminRepository.findAll();
		List<AdminDto> adminDtos = AdminDto.convertToListDto(admins);
		return adminDtos;
	}

	@PostMapping("/grant-access")
	public ResponseEntity<ResponseDto> grantAccess(@RequestBody AccessDto accessDto) {
		/**
		 * 1. find user by email address
		 * 2. determine former role
		 * 3. compare to new role
		 * 4. is there a role change
		 * 4a. no: update user acesss
		 * 4b. yes:
		 * - delete user from previous entity
		 * - create user in new entity
		 * - grant access
		 */
		ResponseDto responseDto = new ResponseDto();
		User user = userRepository.findUserByUsername(accessDto.getEmail());
		User formerUser = user; // useful on the front end to filter off from list of access grants
		String formerRole = user.getRole();

		String newRole = accessDto.getRole();
		String accessMessage = accessDto.getAccessMessage();
		String name = "user";

		Admin admin = new Admin();
		Manager manager = new Manager();
		Employee employee = new Employee();

		// get root admin
		Admin rootAdmin = adminRepository.findByName("ROOT ADMIN");

		// get base manager: this will be assigned to managers and admins converted to
		// employees
		List<Manager> managers = managerRepository.findAll();
		Manager rootManager = managers.get(0);
		

		if (formerRole.equalsIgnoreCase("EMPLOYEE")) {
			employee = employeeRepository.findByUserId(user.getId());
			name = employee.getName();
		} else if (formerRole.equalsIgnoreCase("MANAGER")) {
			manager = managerRepository.findByUserId(user.getId());
			name = manager.getName();
		} else if (formerRole.equalsIgnoreCase("ADMIN")) {
			admin = adminRepository.findByUserId(user.getId());
			name = admin.getName();
		}

		if (newRole.equals(formerRole)) {
			user.setEnabled(true);
			user = userRepository.save(user);
			responseDto.setMsg(formerRole + " with email: " + user.getUsername() + " granted access");
		} else {
			
			System.out.println(formerRole + " " + newRole);
			// SCENARIO 1: converting EMPLOYEE to MANAGER
			if (formerRole.equalsIgnoreCase("EMPLOYEE")
					&& newRole.equalsIgnoreCase("MANAGER")) {
				System.out.println(1);
				Manager newManager = new Manager();
				newManager.setUser(user);
				newManager.setName(employee.getName());
				newManager.setJobTitle(employee.getJobTitle());
				newManager.setAdmin(rootAdmin);
				newManager = managerRepository.save(newManager);

				employeeRepository.deleteById(employee.getId());
				user.setEnabled(true);
				user.setRole(newRole);
				user = userRepository.save(user);

				
				responseDto.setMsg("Former" + formerRole + " with email: "
						+ user.getUsername()
						+ " granted access ("
						+ newRole + ")");
			}

			// SCENARIO 2: converting EMPLOYEE to ADMIN
			if (formerRole.equalsIgnoreCase("EMPLOYEE")
					&& newRole.equalsIgnoreCase("ADMIN")) {
				System.out.println(2);
				Admin newAdmin = new Admin();
				newAdmin.setUser(user);
				newAdmin.setName(employee.getName());
				newAdmin.setJobTitle(employee.getJobTitle());
				newAdmin = adminRepository.save(newAdmin);

				employeeRepository.deleteById(employee.getId());
				user.setEnabled(true);
				user.setRole(newRole);
				user = userRepository.save(user);

				
				responseDto.setMsg("Former " + formerRole + " with email: "
						+ user.getUsername()
						+ " granted access ("
						+ newRole + ")");
			}

			// SCENARIO 3: converting MANAGER to EMPLOYEE
			if (formerRole.equalsIgnoreCase("MANAGER")
					&& newRole.equalsIgnoreCase("EMPLOYEE")) {
				System.out.println(3);
				Employee newEmployee = new Employee();
				newEmployee.setJobTitle(manager.getJobTitle());
				newEmployee.setName(manager.getName());
				newEmployee.setLeavesLeft(20);
				newEmployee.setTotalLeaves(20);
				newEmployee.setManager(rootManager);
				newEmployee.setCreatedOn(LocalDate.now());
				newEmployee.setUser(user);
				newEmployee = employeeRepository.save(newEmployee);

				managerRepository.deleteById(manager.getId());

				user.setEnabled(true);
				user.setRole(newRole);
				user = userRepository.save(user);

				
				responseDto.setMsg("Former " + formerRole + " with email: "
						+ user.getUsername()
						+ " granted access ("
						+ newRole + ")");
			}

			// SCENARIO 4: converting MANAGER to ADMIN
			if (formerRole.equalsIgnoreCase("MANAGER")
					&& newRole.equalsIgnoreCase("ADMIN")) {
				Admin newAdmin = new Admin();
				newAdmin.setJobTitle(manager.getJobTitle());
				newAdmin.setName(manager.getName());
				newAdmin.setUser(user);
				newAdmin = adminRepository.save(newAdmin);

				managerRepository.deleteById(manager.getId());

				user.setEnabled(true);
				user.setRole(newRole);
				user = userRepository.save(user);

				
				responseDto.setMsg("Former " + formerRole + " with email: "
						+ user.getUsername()
						+ " granted access ("
						+ newRole + ")");
			}

			// SCENARIO 5: converting ADMIN to EMPLOYEE
			if (formerRole.equalsIgnoreCase("ADMIN")
					&& newRole.equalsIgnoreCase("EMPLOYEE")) {
				try {

					Employee newEmployee = new Employee();
					newEmployee.setJobTitle(admin.getJobTitle());
					newEmployee.setName(admin.getName());
					newEmployee.setLeavesLeft(20);
					newEmployee.setTotalLeaves(20);
					newEmployee.setManager(rootManager);
					newEmployee.setCreatedOn(LocalDate.now());
					newEmployee.setUser(user);
					newEmployee = employeeRepository.save(newEmployee);

					adminRepository.deleteById(admin.getId());

					user.setEnabled(true);
					user.setRole(newRole);
					user = userRepository.save(user);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				

				responseDto.setMsg("Former " + formerRole + " with email: "
						+ user.getUsername()
						+ " granted access ("
						+ newRole + ")");
			}

			// SCENARIO 6: converting ADMIN to MANAGER
			if (formerRole.equalsIgnoreCase("ADMIN")
					&& newRole.equalsIgnoreCase("MANAGER")) {
				Manager newManager = new Manager();
				newManager.setUser(user);
				newManager.setName(admin.getName());
				newManager.setJobTitle(admin.getJobTitle());
				newManager.setAdmin(rootAdmin);
				newManager = managerRepository.save(newManager);

				adminRepository.deleteById(admin.getId());

				user.setEnabled(true);
				user.setRole(newRole);
				user = userRepository.save(user);

				
				responseDto.setMsg("Former " + formerRole + " with email: "
						+ user.getUsername()
						+ " granted access ("
						+ newRole + ")");
			}
		}

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
		
		user.setRole(formerRole);
		responseDto.setData(formerUser);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);

	}

	@GetMapping("/admin-unenabled")
	public List<AdminDto> getAllAdminsByAccess() {
		List<Admin> admins = adminRepository.findAdminByEnabled(false);
		List<AdminDto> adminDtos = AdminDto.convertToListDto(admins);
		return adminDtos;
	}

	@GetMapping("/ticket/all")
	public List<AdminDto> getAllTickets() {
		List<Admin> admins = adminRepository.findAdminByEnabled(false);
		List<AdminDto> adminDtos = AdminDto.convertToListDto(admins);
		return adminDtos;
	}

	
}