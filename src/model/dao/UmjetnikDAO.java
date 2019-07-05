package model.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
//import net.etfbl.is.pozoriste.controller.PregledRadnikaController;
import model.dto.Angazman;
import model.dto.Biletar;
import model.dto.Radnik;
import model.dto.Umjetnik;
import util.ProtocolMessages;

public class UmjetnikDAO {

    public static void dodajUmjetnika(Umjetnik umjetnik) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeUmjetnika(?,?,?,?,?,?)}");
            callableStatement.setString(1, umjetnik.getIme());
            callableStatement.setString(2, umjetnik.getPrezime());
            callableStatement.setString(3, umjetnik.getJmb());
            callableStatement.setString(4, umjetnik.getKontakt());
            callableStatement.setString(5, umjetnik.getBiografija());
            callableStatement.registerOutParameter(6, Types.INTEGER);

            callableStatement.executeQuery();

            umjetnik.setIdRadnika(callableStatement.getInt(6));
        } catch (SQLException ex) {
            Logger.getLogger(UmjetnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UmjetnikDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String ubaciUTabeluRadnik() {

        System.out.println("BLA BLA BLA BLA");
        String response=ProtocolMessages.UBACI_U_TABELU_RADNIK_UMJETNIK_RESPONSE.getMessage();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Umjetnik umjetnik;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from umjetnici_info");
            while (rs.next()) {
            	String status=rs.getBoolean("StatusRadnika")==true?"true":"false";
            	response+=rs.getString("Ime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("Prezime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+
            			rs.getString("JMB")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+status+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("Kontakt")+
            			ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("Biografija")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+
            			rs.getInt("Id")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException ex) {
            //Logger.getLogger(PregledRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(PregledRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(response.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3) {
        	return null;
        }
        else
        	return response;
    }

    public static void izmjeniUmjetnika(Umjetnik umjetnik) {

        System.out.println("IZMJENA UMJETNIKA");
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeUmjetnika(?,?,?,?,?,?)}");

            callableStatement.setString(1, umjetnik.getIme());
            callableStatement.setString(2, umjetnik.getPrezime());
            callableStatement.setString(3, umjetnik.getJmb());
            callableStatement.setInt(4, umjetnik.getIdRadnika());
            callableStatement.setBoolean(5, umjetnik.isStatusRadnika());
            callableStatement.setString(6, umjetnik.getBiografija());

            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(UmjetnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UmjetnikDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String umjetnici() {
        String umjetnici = ProtocolMessages.UMJETNICI_RESPONSE.getMessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  vratiUmjetnike";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Umjetnik(String ime, String prezime, String jmb, boolean statusRadnika, String kontak, String biografija)
                umjetnici+=resultSet.getString("ime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getString("prezime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getString("jmb")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getBoolean("statusRadnika")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getString("kontakt")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getString("biografija")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getInt("idRadnik")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }
        } catch (SQLException sql) {
            Logger.getLogger(PredstavaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(PredstavaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(PredstavaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(umjetnici.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return  null;
        }
        else {
            return umjetnici;
        }
    }
}