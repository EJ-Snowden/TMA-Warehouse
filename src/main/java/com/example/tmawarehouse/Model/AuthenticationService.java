package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.Data.Emp;

public interface AuthenticationService {
    Emp authenticate(String role, String name, String password);
}
