package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.dto.Angazman;
import model.dto.Predstava;
import util.ProtocolMessages;


public class AngazmanDAO {

    public static String angazmani(Predstava predstava){
        String angazmani = ProtocolMessages.ANGAZMANI_RESPONSE.getMessage();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledAngazmana(?)}");
            callableStatement.setInt(1, predstava.getId());
            resultSet=callableStatement.executeQuery();

            while (resultSet.next()) {
                Date datumOd = resultSet.getDate("datumOd");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String terminOd = formatter.format(datumOd);
                Date datumDo = resultSet.getDate("datumDo");
                String terminDo="null";
                if(datumDo!=null)
                	terminDo = formatter.format(datumDo);
                //Angazman(String ime,String prezime,String vrstaAngazmana,Date datumOd,Date datumDo)
                angazmani+=resultSet.getString("ime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getString("prezime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +resultSet.getString("naziv")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+terminOd+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+terminDo+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(angazmani.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return  null;
        }
        else {
            return angazmani;
        }
    }

    public static void dodajAngazman(Integer idPredstave,Integer idUmjetnika,Integer idVrstaAngazmana,Date datumOd){
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeAngazmana(?,?,?,?)}");
            callableStatement.setInt(1, idPredstave);
            callableStatement.setInt(2, idUmjetnika);
            callableStatement.setInt(3, idVrstaAngazmana);
            callableStatement.setDate(4, datumOd);
            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void azurirajAngazman(Integer idPredstave,Integer idUmjetnika,Integer idVrstaAngazmana,Date datumOd,Date datumDo){
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeAngazmana(?,?,?,?,?)}");
            callableStatement.setInt(1, idVrstaAngazmana);
            callableStatement.setInt(2, idPredstave);
            callableStatement.setInt(3, idUmjetnika);
            callableStatement.setDate(4, datumOd);
            callableStatement.setDate(5, datumDo);
            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}