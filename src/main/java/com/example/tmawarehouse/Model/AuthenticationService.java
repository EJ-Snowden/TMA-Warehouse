package com.example.tmawarehouse.Model;

public interface AuthenticationService {
    boolean authenticate(String role, String name, String password);
}
