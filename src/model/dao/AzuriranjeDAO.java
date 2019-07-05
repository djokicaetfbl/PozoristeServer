package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dto.Azuriranje;

public class AzuriranjeDAO {
    public static void dodajAzuriranje(Azuriranje azuriranje) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeAzuriranja(?,?,?,?)}");
            callableStatement.setObject(1, azuriranje.getIdPredstave());
            callableStatement.setObject(2, azuriranje.getIdRepertoara());
            callableStatement.setObject(3, azuriranje.getIdGostujucePredstave());
            callableStatement.setInt(4, azuriranje.getIdRadnik());

            callableStatement.executeQuery();


        } catch (SQLException ex) {
            Logger.getLogger(AzuriranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AzuriranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}