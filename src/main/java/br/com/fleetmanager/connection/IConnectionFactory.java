package br.com.fleetmanager.connection;

import java.sql.Connection;

public interface IConnectionFactory {

    Connection getNewConnection();
    void UpdateDatabase();

}
