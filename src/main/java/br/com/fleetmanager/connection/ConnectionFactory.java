package br.com.fleetmanager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getNewConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost/camelo","postgres", "0098");
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

}
