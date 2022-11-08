package com.playground.api.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.api.dto.ReqTicketDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.dto.TicketDto;
import com.playground.api.enums.PriorityEnum;
import com.playground.api.enums.TicketStatus;
import com.playground.api.model.Employee;
import com.playground.api.model.Ticket;
import com.playground.api.repositories.EmployeeRepository;
import com.playground.api.repositories.TicketRepository;

@RestController
@RequestMapping("/api/ticket")
 public class TicketController {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private TicketRepository ticketRepository; 
	@Autowired
	private ResponseDto responseDto;
	
	@PostMapping("/add")
	public Ticket postTicket(@RequestBody TicketDto dto, Principal principal) {
		String username = principal.getName();
		
		/* Assigning dto values to Ticket model */
		Ticket ticket = new Ticket();
		ticket.setIssue(dto.getIssue());
		ticket.setPriority(PriorityEnum.valueOf(dto.getPriority()));
		ticket.setGenaratedDate(LocalDate.now());
		ticket.setStatus(TicketStatus.OPEN);
		
		/* Fetch employee by Username */
		Employee employee = employeeRepository.getByEmail(username);
		
		/* Attach employee to ticket */
		ticket.setEmployee(employee);
		
		/* persist ticket */
		ticket  = ticketRepository.save(ticket);
		
		/* Send the response */
		 
		return  ticket;
	}
	
	@GetMapping("/priority/all")
	public List<String> getAllPriorities() {
		List<String> list = new ArrayList<>();
		for(PriorityEnum val : PriorityEnum.values()) {
			list.add(val.toString());
		}
		
		return list; 
	}
	
	@GetMapping("/status/{status}")
	public List<TicketDto> getAllTickets(Principal principal, @PathVariable("status") String status) {
		String username =  principal.getName();
		/* convert status string into enum*/
		TicketStatus statusEnum = TicketStatus.valueOf(status);
		
		List<Ticket> list =ticketRepository.getAllTicketsByUsernameAndStatus(username,statusEnum);
		List<TicketDto> listDto = TicketDto.convertToListDto(list);
		return listDto;
	}
	
	@PutMapping("/status/update")
	public ResponseEntity<ResponseDto> updateTicketStatus(@RequestBody ReqTicketDto dto) {
		/* Fetch Ticket from DB by ticketID */
		Optional<Ticket> optional = ticketRepository.findById(dto.getTicketId());
		if(!optional.isPresent()) {
			responseDto.setMsg("Ticket ID Invalid");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
		}
		Ticket ticket = optional.get();
		
		/* Update the status */
		ticket.setStatus(TicketStatus.valueOf(dto.getStatus()));
		
		/* Save it back */
		ticketRepository.save(ticket);
		responseDto.setMsg("Ticket Closed");
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);

	}
	
	@GetMapping("/all")
	public List<TicketDto> getAllTicketsByManagerAndStatus(Principal principal) {
		String managerUsername = principal.getName();
		
		List<Employee> list =employeeRepository.getAllEmployeeByManager(managerUsername);
		List<Ticket> finalList = new ArrayList<>();
		for(Employee e : list) {
			List<Ticket> listTickets = ticketRepository
			.getAllTicketsByUsernameAndStatus(e.getUser().getUsername(), TicketStatus.OPEN);	
		
			finalList.addAll(listTickets);		
		}
		List<TicketDto> listDto = TicketDto.convertToListDto(finalList);
		return listDto; 
	}
	
	@PutMapping("/response")
	public ResponseEntity<ResponseDto> updateTicketResponse(@RequestBody ReqTicketDto dto) {
		Optional<Ticket> optional = ticketRepository.findById(dto.getTicketId());
		if(!optional.isPresent()) {
			responseDto.setMsg("Ticket ID Invalid");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
		}
		Ticket ticket = optional.get();
		ticket.setResponse(dto.getResponse());
		ticketRepository.save(ticket);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@GetMapping("/all-admin")
	public List<TicketDto> getAllTicketsStatus() {
		
		List<Ticket> tickets = ticketRepository.findAllByStatus(TicketStatus.OPEN);
		List<TicketDto> listDto = TicketDto.convertToListDto(tickets);
		return listDto; 
	}


}
