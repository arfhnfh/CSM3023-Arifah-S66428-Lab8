package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.model.Car;

public class CarDAO {
    
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/carshop";
    private String username = "root";
    private String password = "admin";
    
    private static final String INSERT_CAR = "INSERT INTO CarPricelist (brand, model, cylinder, price) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_CARS = "SELECT * FROM CarPricelist";
    private static final String SELECT_CAR_BY_ID = "SELECT * FROM CarPricelist WHERE Car_id = ?";
    private static final String UPDATE_CAR = "UPDATE CarPricelist SET brand = ?, model = ?, cylinder = ?, price = ? WHERE Car_id = ?";
    private static final String DELETE_CAR = "DELETE FROM CarPricelist WHERE Car_id = ?";

    public CarDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void insertCar(Car car) {
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAR)) {
                preparedStatement.setString(1, car.getBrand());
                preparedStatement.setString(2, car.getModel());
                preparedStatement.setInt(3, car.getCylinder());
                preparedStatement.setDouble(4, car.getPrice());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<Car> selectAllCars() {
        List<Car> cars = new ArrayList<>();
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CARS);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = new Car();
                    car.setId(resultSet.getInt("Car_id"));
                    car.setBrand(resultSet.getString("brand"));
                    car.setModel(resultSet.getString("model"));
                    car.setCylinder(resultSet.getInt("cylinder"));
                    car.setPrice(resultSet.getDouble("price"));
                    cars.add(car);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cars;
    }
    
    public Car selectCarByID(int id) {
        Car car = null;
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CAR_BY_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String brand = resultSet.getString("brand");
                        String model = resultSet.getString("model");
                        int cylinder = resultSet.getInt("cylinder");
                        double price = resultSet.getDouble("price");
                        car = new Car(id, brand, model, cylinder, price);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return car;
    }
    
    public boolean updateCar(Car car) {
        boolean rowUpdated = false;
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAR)) {
                preparedStatement.setString(1, car.getBrand());
                preparedStatement.setString(2, car.getModel());
                preparedStatement.setInt(3, car.getCylinder());
                preparedStatement.setDouble(4, car.getPrice());
                preparedStatement.setInt(5, car.getId());
                rowUpdated = preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowUpdated;
    }
    
    public boolean deleteCar(int id) {
        boolean rowDeleted = false;
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CAR)) {
                preparedStatement.setInt(1, id);
                rowDeleted = preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowDeleted;
    }
}
