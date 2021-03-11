package br.com.fleetmanager.dao;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends ABaseDAO {

    public VehicleDAO(Connection pConnection) {
        super(pConnection);
    }

    @Override
    public boolean Save(Object object) {
        if (((Vehicle) object).getId() > 0)
            return Update(object);
        return Insert(object);
    }

    @Override
    public boolean Delete(Object object) {
        return false;
    }

    @Override
    public boolean Insert(Object object) {
        String sql = ("INSERT INTO VEICULO (placa, descricao, anoFabricacao) VALUES (?, ?, ?)");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((Vehicle) object).getPlate());
            pStmt.setString(2, ((Vehicle) object).getDescription());
            pStmt.setString(3, ((Vehicle) object).getYearFabricated());
            pStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Update(Object object) {
        String sql = ("UPDATE VEICULO SET placa = ?, descricao = ?, anoFabricacao = ? WHERE id = ?");
        try(PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pStmt.setString(1, ((Vehicle) object).getPlate());
            pStmt.setString(2, ((Vehicle) object).getDescription());
            pStmt.setString(3, ((Vehicle) object).getYearFabricated());
            pStmt.setInt(4, ((Vehicle) object).getId());
            pStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public List ListAll() throws SQLException {

        List<Vehicle> vehicles = new ArrayList<>();
        String sql = ("SELECT * FROM VEICULO ORDER BY id ASC");

        try(PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.execute();

            try(ResultSet rSet = pStmt.getResultSet()) {
                while(rSet.next()) {
                    Vehicle vehicle = new Vehicle(
                            rSet.getInt(1),
                            rSet.getString(2),
                            rSet.getString(3),
                            rSet.getString(4));
                    vehicles.add(vehicle);
                }
            }
        }

        return vehicles;
    }
}
