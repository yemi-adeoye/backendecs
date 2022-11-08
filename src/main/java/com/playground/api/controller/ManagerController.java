package com.playground.api.controller;

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

import com.playground.api.dto.ManagerDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.dto.SignUpDto;
import com.playground.api.model.Admin;
import com.playground.api.model.Manager;
import com.playground.api.model.User;
import com.playground.api.repositories.ManagerRepository;
import com.playground.api.repositories.UserRepository;

@RestController
@RequestMapping("/api/manager")
 public class ManagerController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostMapping("/add")
	public ResponseEntity<ResponseDto> addManager(@RequestBody SignUpDto dto, User user, Admin admin) {
		
		
		/* Save manager*/
		Manager manager = new Manager();
		manager.setUser(user);
		manager.setAdmin(admin);
		manager.setName(dto.getName());
		manager.setImageUrl(dto.getImageUrl());
		manager.setJobTitle(dto.getJobTitle());
		
		managerRepository.save(manager);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setData(manager);
		responseDto.setMsg("Manager added to the DB");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
	
	/*@PostMapping("/add")
	public ResponseEntity<String> addManager(@RequestBody ManagerDto dto) {
		// Save User 
		User user = new User(); //POJO
		user.setUsername(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRole(dto.getRole());
		user.setEnabled(true);
		
		user.setPassword(encoder.encode(dto.getPassword()));
		user = userRepository.save(user);
		// Save manager
		Manager manager = new Manager();
		manager.setUser(user);
		manager.setName(dto.getName());
		manager.setImageUrl(dto.getImageUrl());
		manager.setJobTitle(dto.getJobTitle());
		
		managerRepository.save(manager);
		return ResponseEntity.status(HttpStatus.OK).body("Manager added to the DB");
	}
	*/
	@GetMapping("/all")
	public List<ManagerDto> getAllManager() {
		List<Manager> list =  managerRepository.findAll();
		List<ManagerDto>  listDto = ManagerDto.convertToListDto(list);
		return listDto;
	}

	@GetMapping("/access")
	public List<ManagerDto> getEmployeeHavingAccess() {
		List<Manager> list = managerRepository.findManagerByEnabled(false);
		List<ManagerDto> listDto = ManagerDto.convertToListDto(list);
		return listDto;
	}
	
	
}
