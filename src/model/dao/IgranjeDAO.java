package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dto.Igranje;
import model.dto.Repertoar;
import util.ProtocolMessages;

/**
 *
 * @author Ognjen
 */
public class IgranjeDAO {

    public static void dodajIgranje(Igranje igranje) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeIgranja(?,?,?,?,?)}");
            callableStatement.setDate(1, igranje.getTermin());
            callableStatement.setInt(2, igranje.getIdScene());
            callableStatement.setObject(3, igranje.getIdGostujucePredstave());
            callableStatement.setObject(4, igranje.getIdPredstave());
            callableStatement.setInt(5, igranje.getIdRepertoara());

            callableStatement.executeQuery();

        } catch (SQLException ex) {
            //Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                   // Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String getIgranja(int idRepertoara) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        String igranja = ProtocolMessages.GET_IGRANJA_RESPONSE.getMessage();
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledIgranjaZaOdredjeniRepertoar(?)}");
            callableStatement.setInt(1, idRepertoara);
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                Date termin = resultSet.getDate("termin");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(termin);
                Integer idS = resultSet.getInt("idScene");
                Integer idGost = resultSet.getInt("idGostujucePredstave");
                Integer idP = resultSet.getInt("idPredstave");
                Integer idR = resultSet.getInt("idRepertoara");
                //Igranje(termin, idS, idP, idGost, idR)
                igranja+=strDate+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+idS+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+idP
                        +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+idGost+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+idR+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }
        } catch (SQLException sql) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(igranja.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return null;
        }
        else {
            return igranja;
        }
    }

    public static LinkedList<Igranje> getIgranjaBP(int idRepertoara) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        LinkedList<Igranje> igranja = new LinkedList<>();
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledIgranjaZaOdredjeniRepertoar(?)}");
            callableStatement.setInt(1, idRepertoara);
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                Date termin = resultSet.getDate("termin");
                Integer idS = resultSet.getInt("idScene");
                Integer idGost = resultSet.getInt("idGostujucePredstave");
                Integer idP = resultSet.getInt("idPredstave");
                Integer idR = resultSet.getInt("idRepertoara");
                igranja.add(new Igranje(termin, idS, idP, idGost, idR));
            }
        } catch (SQLException sql) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return igranja;
    }


    public static void UkloniIgranje(Igranje igranje) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionPool.getInstance().checkOut();
            String query = "delete from igranje  where termin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, igranje.getTermin());
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}