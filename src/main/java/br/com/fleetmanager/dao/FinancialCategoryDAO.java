package br.com.fleetmanager.dao;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.utils.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinancialCategoryDAO extends ABaseDAO {

    public FinancialCategoryDAO(Connection pConnection) {
        super(pConnection);
    }

    public FinancialCategoryDAO() {
    }

    @Override
    public boolean Save(Object object) {
        if (((FinancialCategory) object).getId() > 0)
            return Update(object);
        return Insert(object);
    }

    @Override
    public boolean Delete(int id) {
        String sql = ("UPDATE CATEGORIAFINANCEIRA SET status = ? WHERE id = ?");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setShort(1, Constants.cStatusInactive);
            pStmt.setInt(2, id);
            pStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Insert(Object object) {
        String sql = ("INSERT INTO CATEGORIAFINANCEIRA (descricao, tipo) VALUES (?, ?)");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((FinancialCategory) object).getDescription());
            pStmt.setShort(2, ((FinancialCategory) object).getType());
            pStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Update(Object object) {
        String sql = ("UPDATE CATEGORIAFINANCEIRA SET descricao = ?, tipo = ? WHERE id = ?");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((FinancialCategory) object).getDescription());
            pStmt.setShort(2, ((FinancialCategory) object).getType());
            pStmt.setInt(3, ((FinancialCategory) object).getId());
            pStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public List ListAll() throws SQLException {

        List<FinancialCategory> categories = new ArrayList<>();
            String sql = ("SELECT id, status, descricao, tipo FROM CATEGORIAFINANCEIRA WHERE (status = ?) ORDER BY id ASC");

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
        }

        return categories;
    }

}
