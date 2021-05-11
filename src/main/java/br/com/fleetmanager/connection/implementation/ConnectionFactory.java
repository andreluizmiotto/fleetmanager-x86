package br.com.fleetmanager.connection.implementation;

import br.com.fleetmanager.Launcher;
import br.com.fleetmanager.connection.IConnectionFactory;
import br.com.fleetmanager.utils.Constants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class ConnectionFactory implements IConnectionFactory {

    private final String databaseURL;
    private final String sqlCreateDatabase;

    public ConnectionFactory() {
        this.databaseURL = "jdbc:sqlite:../database.db";
        this.sqlCreateDatabase = "CreateSQLiteDB.sql";
    }

    public ConnectionFactory(String databaseURL, String sqlCreateDatabase) {
        this.databaseURL = databaseURL;
        this.sqlCreateDatabase = sqlCreateDatabase;
    }

    public Connection getNewConnection() {
        try {
            return DriverManager.getConnection(databaseURL);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public void UpdateDatabase() {
        try (Connection connection = getNewConnection()) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            InputStream inputStream = Launcher.class.getResourceAsStream(Constants.sDataBaseFolder + sqlCreateDatabase);
            String sqlQuery = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

            statement.executeUpdate(sqlQuery);
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
        }
    }
}
