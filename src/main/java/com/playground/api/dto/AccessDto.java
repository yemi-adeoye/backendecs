package com.playground.api.dto;

public class AccessDto {
    private String email;
    private String role;
    private String accessMessage;

    public AccessDto() {
    }

    public AccessDto(String email, String role, String accessMessage) {
        this.email = email;
        this.role = role;
        this.accessMessage = accessMessage;
    }
    

    public String getAccessMessage() {
        return this.accessMessage;
    }

    public void setAccessMessage(String accessMessage) {
        this.accessMessage = accessMessage;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AccessDto email(String email) {
        setEmail(email);
        return this;
    }

    public AccessDto role(String role) {
        setRole(role);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " email='" + getEmail() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }

}
