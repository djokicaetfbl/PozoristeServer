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
import util.ProtocolMessages;

public class ScenaDAO {

    public static String scene() {
        String sceneResponse = ProtocolMessages.SCENE_RESPONSE.getMessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * " + "FROM  scena";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Scena(Integer id,String naziv)
                sceneResponse+=resultSet.getInt("id")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getString("nazivScene")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException sql) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(sceneResponse.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return  null;
        }
        else{
            return  sceneResponse;
        }
    }


    public static boolean dodavanjeScene(String naziv,Integer brojRedova, Integer brojKolona) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeNoveScene(?,?,?)}");
            callableStatement.setString(1, naziv);
            callableStatement.setInt(2, brojRedova);
            callableStatement.setInt(3, brojKolona);

            return callableStatement.executeUpdate() > 0;

        } catch (SQLException sql) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }
}