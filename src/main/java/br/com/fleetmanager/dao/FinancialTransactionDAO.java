package br.com.fleetmanager.dao;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.model.FinancialTransaction;
import br.com.fleetmanager.utils.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinancialTransactionDAO extends ABaseDAO {

    private static final String cTableName = "lancfinanceiro";

    public FinancialTransactionDAO(Connection pConnection) {
        super(pConnection, cTableName);
    }

    public FinancialTransactionDAO() {
        super(cTableName);
    }

    @Override
    public boolean Save(Object object) {
        if (((FinancialTransaction) object).getId() > 0)
            return Update(object);
        return Insert(object);
    }

    @Override
    public boolean Insert(Object object) {
        String sql = ("INSERT INTO " + cTableName + " (data, idveiculo, idcategoria, valor, descricao) VALUES (?, ?, ?, ?, ?)");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setDate(1, ((FinancialTransaction) object).getDate());
            pStmt.setInt(2, ((FinancialTransaction) object).getVehicle().getId());
            pStmt.setInt(3, ((FinancialTransaction) object).getCategory().getId());
            pStmt.setDouble(4, ((FinancialTransaction) object).getValue());
            pStmt.setString(5, ((FinancialTransaction) object).getDescription());
            Execute(pStmt);

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Update(Object object) {
        String sql = ("UPDATE " + cTableName + " SET data = ?, idveiculo = ?, idcategoria = ?, valor = ?, descricao = ? WHERE id = ?");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setDate(1, ((FinancialTransaction) object).getDate());
            pStmt.setInt(2, ((FinancialTransaction) object).getVehicle().getId());
            pStmt.setInt(3, ((FinancialTransaction) object).getCategory().getId());
            pStmt.setDouble(4, ((FinancialTransaction) object).getValue());
            pStmt.setString(5, ((FinancialTransaction) object).getDescription());
            pStmt.setInt(6, ((FinancialTransaction) object).getId());
            Execute(pStmt, ((FinancialTransaction) object).getId());

            return true;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public List<FinancialTransaction> ListAll() {
        return ListByPeriod(null, null);
    }

    @Override
    public List ListByPeriod(Date dtIni, Date dtFin) {
        return ListByPeriod(dtIni, dtFin, 0);
    }

    public List<FinancialTransaction> ListByPeriod(Date dtIni, Date dtFin, int vehicleId) {
        List<FinancialTransaction> transactions = new ArrayList<>();
        String sql = "SELECT id, status, data, idveiculo, idcategoria, valor, descricao FROM " + cTableName +
                     " WHERE (status = ?) ";
        if ((dtIni != null) && (dtFin != null))
            sql += " and (data between ? and ?) ";
        if (vehicleId > 0)
            sql += " and (idveiculo = ?) ";
        sql += "ORDER BY id ASC";

        try(PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.setShort(1, Constants.cStatusActive);
            if ((dtIni != null) && (dtFin != null)) {
                pStmt.setDate(2, dtIni);
                pStmt.setDate(3, dtFin);
            }
            if (vehicleId > 0)
                pStmt.setInt(4, vehicleId);
            pStmt.execute();

            try(ResultSet rSet = pStmt.getResultSet()) {
                while(rSet.next()) {
                    FinancialTransaction transaction = new FinancialTransaction(
                            rSet.getInt(1),
                            rSet.getShort(2),
                            rSet.getDate(3),
                            new VehicleDAO(connection).Find(rSet.getInt(4)),
                            new FinancialCategoryDAO(connection).Find(rSet.getInt(5)),
                            rSet.getDouble(6),
                            rSet.getString(7));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
        return transactions;
    }

    @Override
    public Object Find(int id) {
        return null;
    }

}
