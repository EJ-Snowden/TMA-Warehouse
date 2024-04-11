package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.MainApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationClass implements AuthenticationService{
    static Statement statement;
    @Override
    public boolean authenticate(String role, String name, String password) {
        try {
            if (MainApplication.getDBHelper().getConnection() != null) {
                statement = MainApplication.getDBHelper().getConnection().createStatement();
                String query = "SELECT EmpName, EmpRole, Password FROM Emp";
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                    if (name.equalsIgnoreCase(rs.getString("EmpName")) && role.equalsIgnoreCase(rs.getString("EmpRole")))
                        return password.equals(rs.getString("Password"));
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
        return false;
    }
}
