package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.Data.Emp;
import com.example.tmawarehouse.MainApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationClass implements AuthenticationService{
    static Statement statement;
    @Override
    public Emp authenticate(String role, String name, String password) {
        try {
            if (MainApplication.getDBHelper().getConnection() != null) {
                statement = MainApplication.getDBHelper().getConnection().createStatement();
                String query = "SELECT EmpId, EmpName, EmpRole, Password FROM Emp";
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                    if (name.equalsIgnoreCase(rs.getString("EmpName")) && role.equalsIgnoreCase(rs.getString("EmpRole"))) {
                        return new Emp(rs.getInt("EmpID"), rs.getString("EmpName"), rs.getString("EmpRole"));
                    }
                }
                statement.close();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
