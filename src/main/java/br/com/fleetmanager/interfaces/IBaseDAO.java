package br.com.fleetmanager.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IBaseDAO<T> {

    public boolean Insert(T object);
    public boolean Delete(T object);
    public boolean Update(T object);
    public List<T> ListAll() throws SQLException;

}
