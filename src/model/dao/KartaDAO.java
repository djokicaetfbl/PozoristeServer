package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import model.dto.Karta;
import model.dto.Predstava;
import util.ProtocolMessages;

public class KartaDAO {

    public static String karte() {
        String karte = ProtocolMessages.KARTE_RESPONSE.getMessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  karta";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                karte+=resultSet.getInt("id") +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+ resultSet.getInt("brojReda")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getInt("brojSjedista")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getDate("termin")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getInt("idScene")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }
        } catch (SQLException sql) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(karte.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return  null;
        }
        else{
            return karte;
        }
    }

    public static boolean dodajKartu(Karta karta) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeKarte(?,?,?,?,?)}");
            callableStatement.setInt(1, karta.getBrojReda());
            callableStatement.setInt(2, karta.getBrojSjedista());
            callableStatement.setFloat(3, karta.getIznos());
            callableStatement.setDate(4, karta.getTermin());
            callableStatement.setInt(5, karta.getIdScene());

            callableStatement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }


    public static boolean obrisiKartu(final int idKarte){
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call storniranjeKarte(?)}");
            callableStatement.setInt(1, idKarte);

            callableStatement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }


}