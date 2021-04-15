package br.com.fleetmanager.dao;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.model.Vehicle;
import br.com.fleetmanager.utils.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends ABaseDAO {

    private static final String cTableName = "veiculo";

    public VehicleDAO(Connection pConnection) {
        super(pConnection, cTableName);
    }

    public VehicleDAO() {
        super(cTableName);
    }

    @Override
    public boolean Save(Object object) {
        if (((Vehicle) object).getId() > 0)
            return Update(object);
        return Insert(object);
    }

    @Override
    public boolean Insert(Object object) {
        String sql = ("INSERT INTO " + cTableName + " (placa, descricao, anoFabricacao) VALUES (?, ?, ?)");
        try (PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((Vehicle) object).getPlate());
            pStmt.setString(2, ((Vehicle) object).getDescription());
            pStmt.setString(3, ((Vehicle) object).getYearFabricated());
            Execute(pStmt);

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Update(Object object) {
        String sql = ("UPDATE " + cTableName + " SET placa = ?, descricao = ?, anoFabricacao = ? WHERE id = ?");
        try (PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((Vehicle) object).getPlate());
            pStmt.setString(2, ((Vehicle) object).getDescription());
            pStmt.setString(3, ((Vehicle) object).getYearFabricated());
            pStmt.setInt(4, ((Vehicle) object).getId());
            Execute(pStmt, ((Vehicle) object).getId());

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
    public List<Vehicle> ListAll() {

        List<Vehicle> vehicles = new ArrayList<>();
        String sql = ("SELECT id, status, placa, descricao, anoFabricacao FROM " + cTableName + " WHERE (status = ?) ORDER BY id ASC");

        try (PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.setShort(1, Constants.cStatusActive);
            pStmt.execute();

            try (ResultSet rSet = pStmt.getResultSet()) {
                while (rSet.next()) {
                    Vehicle vehicle = new Vehicle(
                            rSet.getInt(1),
                            rSet.getShort(2),
                            rSet.getString(3),
                            rSet.getString(4),
                            rSet.getString(5));
                    vehicles.add(vehicle);
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
        return vehicles;
    }

    @Override
    public Vehicle Find(int id) {
        String sql = ("SELECT id, status, placa, descricao, anoFabricacao FROM " + cTableName + " WHERE (id = ?)");

        try (PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.setInt(1, id);
            pStmt.execute();

            try (ResultSet rSet = pStmt.getResultSet()) {
                if (!rSet.next())
                    return null;
                return new Vehicle(
                        rSet.getInt(1),
                        rSet.getShort(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5));
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
