package com.DAO;

import com.Model.Employee;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private Connection connection = null;
    private String url = "jdbc:mysql://localhost:3306/csm3023lab8";
    private String username = "root";
    private String password = "admin";

    private static final String INSERT_EMPLOYEE = "INSERT INTO employee (name, email, position) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_EMPLOYEES = "SELECT * FROM employee";
    private static final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employee WHERE id = ?";
    private static final String UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, email = ?, position = ? WHERE id = ?";
    private static final String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?";

    public EmployeeDAO() {
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            System.out.println("Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Establishing connection to the database...");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to establish a database connection: SQLException");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySQL JDBC driver not found: ClassNotFoundException");
        }
        return connection;
    }

    public void insertEmployee(Employee employee) {
        System.out.println(INSERT_EMPLOYEE);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE)) {
            if (connection == null) {
                System.out.println("Failed to establish a database connection in insertEmployee method.");
                return;
            }
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getPosition());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<Employee> selectAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EMPLOYEES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (connection == null) {
                System.out.println("Failed to establish a database connection in selectAllEmployees method.");
                return employees;
            }
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setEmail(resultSet.getString("email"));
                employee.setPosition(resultSet.getString("position"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return employees;
    }

    public Employee selectEmployeeByID(int id) {
        Employee employee = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
            if (connection == null) {
                System.out.println("Failed to establish a database connection in selectEmployeeByID method.");
                return null;
            }
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String position = resultSet.getString("position");
                employee = new Employee(id, name, email, position);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return employee;
    }

    public boolean updateEmployee(Employee employee) {
        boolean rowUpdated = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE)) {
            if (connection == null) {
                System.out.println("Failed to establish a database connection in updateEmployee method.");
                return false;
            }
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getPosition());
            preparedStatement.setInt(4, employee.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rowUpdated;
    }

    public boolean deleteEmployee(int id) {
        boolean rowDeleted = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE)) {
            if (connection == null) {
                System.out.println("Failed to establish a database connection in deleteEmployee method.");
                return false;
            }
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rowDeleted;
    }

    public void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
