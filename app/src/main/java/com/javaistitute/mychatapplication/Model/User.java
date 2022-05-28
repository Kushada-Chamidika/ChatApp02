package com.javaistitute.mychatapplication.Model;

import java.util.List;

public class User {
    public User() {
    }

    public User(String u_id, String username, String email, String password) {
        this.u_id = u_id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getU_id() {
        return u_id;
    }

    String u_id;
    String username;
    String email;
    String password;
}
