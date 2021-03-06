package br.com.fleetmanager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public ConnectionFactory() {

    }

    public Connection getNewConnection() throws SQLException {
        return DriverManager
                .getConnection
                        ("jdbc:postgresql://localhost/camelo",
                                "postgres", "0098");
    }


}
