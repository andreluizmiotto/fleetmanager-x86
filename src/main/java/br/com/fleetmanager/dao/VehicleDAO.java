package br.com.fleetmanager.dao;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends ABaseDAO {

    public VehicleDAO(Connection pConnection) {
        super(pConnection);
    }

    @Override
    public boolean Insert(Object object) {
        return false;
    }

    @Override
    public boolean Delete(Object object) {
        return false;
    }

    @Override
    public boolean Update(Object object) {
        return false;
    }

    @Override
    public List ListAll() throws SQLException {

        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        String sql = ("SELECT ID, PLACA, DESCRICAO, ANOFABRICACAO FROM VEICULO");

        try(PreparedStatement pStmt = connection.prepareStatement(sql)) {
            pStmt.execute();

            try(ResultSet rSet = pStmt.getResultSet()) {
                while(rSet.next()) {
                    Vehicle vehicle = new Vehicle(
                            rSet.getInt(1),
                            rSet.getString(2),
                            rSet.getString(3),
                            rSet.getInt(4));
                    vehicles.add(vehicle);
                }
            }
        }

        return vehicles;
    }
}
