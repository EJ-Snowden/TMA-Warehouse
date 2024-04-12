package com.example.tmawarehouse.Data;

public class Emp {
    private int empId;
    private String empName;
    private String empRole;

    public Emp(int empId, String empName, String empRole) {
        this.empId = empId;
        this.empName = empName;
        this.empRole = empRole;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String empRole) {
        this.empRole = empRole;
    }
}
