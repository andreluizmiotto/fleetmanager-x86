package br.com.fleetmanager.dao;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.utils.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinancialCategoryDAO extends ABaseDAO {

    private static final String cTableName = "categoriafinanceira";

    public FinancialCategoryDAO(Connection pConnection) {
        super(pConnection, cTableName);
    }

    public FinancialCategoryDAO() {
        super(cTableName);
    }

    @Override
    public boolean Save(Object object) {
        if (((FinancialCategory) object).getId() > 0)
            return Update(object);
        return Insert(object);
    }

    @Override
    public boolean Insert(Object object) {
        String sql = ("INSERT INTO " + cTableName + " (descricao, tipo) VALUES (?, ?)");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((FinancialCategory) object).getDescription());
            pStmt.setShort(2, ((FinancialCategory) object).getType());
            Execute(pStmt);

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Update(Object object) {
        String sql = ("UPDATE " + cTableName + " SET descricao = ?, tipo = ? WHERE id = ?");
        try(PreparedStatement pStmt = connection.prepareStatement(sql)) {

            pStmt.setString(1, ((FinancialCategory) object).getDescription());
            pStmt.setShort(2, ((FinancialCategory) object).getType());
            pStmt.setInt(3, ((FinancialCategory) object).getId());
            Execute(pStmt, ((FinancialCategory) object).getId());

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public List ListByPeriod(Date dtIni, Date dtFin) {
        // TODO: 31/03/2021 Implements as in FinancialTransactionDAO
        return ListAll();
    }

    @Override
    public List<FinancialCategory> ListAll() {

        List<FinancialCategory> categories = new ArrayList<>();
        String sql = ("SELECT id, status, descricao, tipo FROM " + cTableName + " WHERE (status = ?) ORDER BY id ASC");

        try(PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.setShort(1, Constants.cStatusActive);
            pStmt.execute();

            try(ResultSet rSet = pStmt.getResultSet()) {
                while(rSet.next()) {
                    FinancialCategory financialCategory = new FinancialCategory(
                            rSet.getInt(1),
                            rSet.getShort(2),
                            rSet.getString(3),
                            rSet.getShort(4));
                    categories.add(financialCategory);
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        return categories;
    }

    @Override
    public FinancialCategory Find(int id) {
        String sql = ("SELECT id, status, descricao, tipo FROM " + cTableName + " WHERE (id = ?)");

        try (PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.setInt(1, id);
            pStmt.execute();

            try (ResultSet rSet = pStmt.getResultSet()) {
                if (!rSet.next())
                    return null;
                return new FinancialCategory(
                        rSet.getInt(1),
                        rSet.getShort(2),
                        rSet.getString(3),
                        rSet.getShort(4));
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
