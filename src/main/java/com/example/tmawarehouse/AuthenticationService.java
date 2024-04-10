package com.example.tmawarehouse;

public interface AuthenticationService {
    boolean authenticate(String role, String name, String password);
}
