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
import model.dto.VrstaAngazmana;
import util.ProtocolMessages;

public class VrstaAngazmanaDAO {
    public static String vrsteAngazmana() {
        String angazmani = ProtocolMessages.VRSTE_ANGAZMANA_RESPONSE.getMessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  pregledVrstaAngazmana";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //VrstaAngazmana(Integer id, String naziv)
                angazmani+=resultSet.getInt("id")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getString("naziv")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }
        } catch (SQLException sql) {
            Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(angazmani.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3)
            return null;
        else {
            return angazmani;
        }
    }

    public static void dodajAngazman(String naziv){
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeVrstaAngazmana(?)}");
            callableStatement.setString(1, naziv);
            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}