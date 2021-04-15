package br.com.fleetmanager.interfaces;

import java.sql.Date;
import java.util.List;

public interface IBaseDAO<T> {

    boolean Save(T object);
    List<T> ListAll();
    List<T> ListByPeriod(Date dtIni, Date dtFin);
    T Find(int id);

    boolean Insert(T object);
    boolean Update(T object);
}
