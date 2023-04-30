package com.agri40.core.dto;

public class UserInfo {

    private String FullName;
    private String Email;

    public String getFullName() {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
