package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dto.RezervisanoSjediste;
import util.ProtocolMessages;

public class RezervisanoSjedisteDAO {


    public static String sjedista(Date termin,Integer idScene) {
        String sjedistaRezervisana = ProtocolMessages.SJEDISTA_RS_RESPONSE.getMessage();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledRezervisanihMjesta(?,?)}");

            callableStatement.setInt(2, idScene);
            callableStatement.setDate(1, termin);
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                sjedistaRezervisana+=resultSet.getInt("idScene")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getInt("brojSjedista")
                        +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getInt("idRezervacije")
                        +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getDate("termin")
                        +ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException sql) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(sjedistaRezervisana.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return null;
        }
        else {
            return sjedistaRezervisana;
        }
    }
    
    public static List<RezervisanoSjediste> sjedistaDAO(Date termin,Integer idScene) {
        List<RezervisanoSjediste> sjedistaRezervisana = new ArrayList<>();
       Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        
        try {
           connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledRezervisanihMjesta(?,?)}");
            callableStatement.setDate(1, termin);
            callableStatement.setInt(2, idScene);
            
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
               RezervisanoSjediste rezervisanoSjediste = new RezervisanoSjediste(
                    resultSet.getInt("idScene"),resultSet.getInt("brojSjedista"),resultSet.getInt("idRezervacije"),resultSet.getDate("termin")); 
               sjedistaRezervisana.add(rezervisanoSjediste);
            }

        } catch (SQLException sql) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return sjedistaRezervisana;
    }
    



    public static boolean addRezervisanoSjediste(RezervisanoSjediste rezervisanoSjediste) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRezervisanogSjedista(?,?,?,?)}");
            callableStatement.setInt(1, rezervisanoSjediste.getBrojSjedista());
            callableStatement.setDate(2, rezervisanoSjediste.getTermin());
            callableStatement.setInt(3, rezervisanoSjediste.getIdScene());
            callableStatement.setInt(4, rezervisanoSjediste.getIdRezervacije());

            int count = callableStatement.executeUpdate();
            if (count <= 0) {
                return false;
            }
        } catch (SQLException sql) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }



    public static boolean obrisiRezervisanoSjediste(RezervisanoSjediste rezervisanoSjediste) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call brisanjeRezervisanogSjedista(?,?,?,?)}");
            callableStatement.setInt(2, rezervisanoSjediste.getBrojSjedista());
            callableStatement.setDate(4, rezervisanoSjediste.getTermin());
            callableStatement.setInt(1, rezervisanoSjediste.getIdScene());
            callableStatement.setInt(3, rezervisanoSjediste.getIdRezervacije());

            int count = callableStatement.executeUpdate();
            if (count <= 0) {
                return false;
            }
        } catch (SQLException sql) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }


}