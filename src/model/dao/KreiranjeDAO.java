package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import model.dto.Kreiranje;
import model.dto.Predstava;

public class KreiranjeDAO {
    public static void dodajKreiranje(Kreiranje kreiranje) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeKreiranja(?,?,?,?)}");
            callableStatement.setObject(1, kreiranje.getIdPredstave());
            callableStatement.setObject(2, kreiranje.getIdRepertoara());
            callableStatement.setObject(3, kreiranje.getIdGostujucePredstave());
            callableStatement.setInt(4, kreiranje.getIdRadnik());

            callableStatement.executeQuery();


        } catch (SQLException ex) {
            Logger.getLogger(KreiranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(KreiranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}