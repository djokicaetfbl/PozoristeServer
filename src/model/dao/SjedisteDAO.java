package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.dto.Scena;
import model.dto.Sjediste;
import util.ProtocolMessages;

public class SjedisteDAO {

    public static String sjedista(Integer idScene) {
        String sjedista = ProtocolMessages.SJEDISTA_RESPONSE.getMessage();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledSjedistaZaScenu(?)}");
            callableStatement.setInt(1, idScene);

            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                sjedista+=resultSet.getInt("brojSjedista")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getString("idScene")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException sql) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(sjedista.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return null;
        }
        else {
            return sjedista;
        }
    }

    public static boolean dodavanjeSjedista(Integer idScene,Integer brojSjedista) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeSjedista(?,?)}");
            callableStatement.setInt(1, idScene);
            callableStatement.setInt(2, brojSjedista);

            callableStatement.executeQuery();
            return true;
        } catch (SQLException sql) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }
}