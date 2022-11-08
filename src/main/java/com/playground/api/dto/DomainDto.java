package com.playground.api.dto;

import com.playground.api.model.Domain;

import java.util.ArrayList;
import java.util.List;

public class DomainDto {

    private String name;
    private String domain;

    private boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "DomainDto{" +
                "name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    public static List<DomainDto> convertToDomainDtoList(List<Domain> list){
        List<DomainDto> domainDtos = new ArrayList<>();
        list.stream().forEach(domain -> {
            DomainDto dto = new DomainDto();
            dto.setName(domain.getName());
            dto.setDomain(domain.getDomain());
            dto.setEnabled(domain.getEnabled());
            domainDtos.add(dto);
        });
        return domainDtos;
    }
}
