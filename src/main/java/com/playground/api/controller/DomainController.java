package com.playground.api.controller;

import com.playground.api.dto.DomainDto;
import com.playground.api.dto.ResponseDto;
import com.playground.api.model.Domain;
import com.playground.api.repositories.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/domain")
public class DomainController {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ResponseDto responseDto;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addDomain(@RequestBody DomainDto dto, Principal principal){

        /* Check if domain name already exist */
        Domain domain = domainRepository.findByDomainName(dto.getDomain());
        responseDto.setMsg("Domain already exists.");

        if(!(domain == null)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        domain = new Domain();
        domain.setName(dto.getName());
        domain.setDomain(dto.getDomain());
        domain.setEnabled(true);

        domainRepository.save(domain);

        responseDto.setMsg("Domain added.");

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/all")
    public List<DomainDto> getAllDomain(){
        List<Domain> list = domainRepository.findAll();
        List<DomainDto> domainDtos = DomainDto.convertToDomainDtoList(list);

        return domainDtos;
    }

    @GetMapping("/isValid/{domainName}")
    public ResponseEntity<ResponseDto> isDomainValid(@PathVariable("domainName") String domainName){

        Domain domain = domainRepository.findByDomainNameAndStatus(domainName, true);
        responseDto.setMsg("Domain is not valid");
        if(domain == null){
            responseDto.setData(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } else{
            responseDto.setMsg("Domain is valid");
            responseDto.setData(domain.getEnabled());
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
    }

    @GetMapping("/disable/{domainName}")
    public ResponseEntity<ResponseDto> disableDomain(@PathVariable("domainName") String domainName){
        Domain domain = domainRepository.findByDomainName(domainName);
        responseDto.setMsg("Domain doesn't exists.");
        if(domain == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } else{
            responseDto.setMsg("Domain is already disabled. ");
            if(domain.getEnabled() == false)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
            domain.setEnabled(false);
            domainRepository.save(domain);

            responseDto.setMsg("Domain disabled.");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
    }

    @GetMapping("/enable/{domainName}")
    public ResponseEntity<ResponseDto> enableDomain(@PathVariable("domainName") String domainName){
        Domain domain = domainRepository.findByDomainName(domainName);
        responseDto.setMsg("Domain doesn't exists.");
        if(domain == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } else{
            responseDto.setMsg("Domain is already enabled. ");
            if(domain.getEnabled() == true)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
            domain.setEnabled(true);
            domainRepository.save(domain);

            responseDto.setMsg("Domain enabled.");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
    }
}
