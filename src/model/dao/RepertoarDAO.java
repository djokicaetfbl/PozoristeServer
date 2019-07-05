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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dto.Repertoar;
import util.ProtocolMessages;

public class RepertoarDAO {

    public static void dodajRepertoar(Repertoar repertoar) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRepertoara(?,?)}");
            Date datum=repertoar.getMjesecIGodina();
            callableStatement.setDate(1, datum);
            callableStatement.registerOutParameter(2, Types.INTEGER);

            callableStatement.executeQuery();
            repertoar.setId(callableStatement.getInt(2));
        } catch (SQLException ex) {
           // Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    //Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

   /* public static Repertoar getRepertoar(final int id) {

        return repertoars().stream().filter(e -> e.getId() == id).findFirst().get();
    }*/

    public static String repertoars() {
        String repertoars = ProtocolMessages.REPERTOARS_RESPONSE.getMessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  repertoari_info";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(resultSet.getDate("mjesecIGodina"));
                repertoars+=resultSet.getInt("id")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+strDate+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException sql) {
            Logger.getLogger(RepertoarDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RepertoarDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RepertoarDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        if(repertoars.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return null;
        }
        else {
            return repertoars;
        }
    }

    public static void izmjeniRepertoar(Repertoar repertoar) {
        System.out.println("IZMJENA Repertoara : : : " + repertoar);

        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeRepertoara(?,?)}");

            callableStatement.setInt(1, repertoar.getId());
            callableStatement.setDate(2, repertoar.getMjesecIGodina());

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

}