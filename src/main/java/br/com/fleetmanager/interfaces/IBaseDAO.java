package br.com.fleetmanager.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IBaseDAO<T> {

    boolean Save(T Object);
    boolean Delete(int id);
    List<T> ListAll() throws SQLException;

    boolean Insert(T Object);
    boolean Update(T Object);
}
