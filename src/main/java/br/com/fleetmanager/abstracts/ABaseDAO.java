package br.com.fleetmanager.abstracts;

import br.com.fleetmanager.interfaces.IBaseDAO;

import java.sql.Connection;

public abstract class ABaseDAO implements IBaseDAO {

    protected Connection connection;

    public ABaseDAO(Connection pConnection) {
        this.connection = pConnection;
    }

    public ABaseDAO() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
