package br.com.fleetmanager.abstracts;

import br.com.fleetmanager.interfaces.IBaseDAO;
import br.com.fleetmanager.utils.Constants;

import java.sql.*;

public abstract class ABaseDAO implements IBaseDAO {

    protected Connection connection;
    private final String tableName;

    public ABaseDAO(Connection pConnection, String pTableName) {
        this.connection = pConnection;
        this.tableName = pTableName;
    }

    public ABaseDAO(String pTableName) {
        this.tableName = pTableName;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean Delete(int id) {
        String sql = ("UPDATE " + tableName + " SET status = ? WHERE id = ?");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setShort(1, Constants.cStatusInactive);
            pStmt.setInt(2, id);
            pStmt.execute();
            Audit(id);

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void Audit(int id) {
        String sql = ("INSERT INTO auditoria (tabela, idRegistro, data) VALUES (?, ?, ?)");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, tableName);
            pStmt.setInt(2, id);
            pStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pStmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Execute(PreparedStatement pStmt) throws SQLException {
        pStmt.execute();
        try (ResultSet keys = pStmt.getGeneratedKeys()) {
            if (keys.next())
                Audit(keys.getInt(1));
        }
    }

    public void Execute(PreparedStatement pStmt, int pId) throws SQLException {
        pStmt.execute();
        Audit(pId);
    }
}
