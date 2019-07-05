package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

import model.dto.GostujucaPredstava;
import model.dto.Umjetnik;
import util.ProtocolMessages;

public class GostujucaPredstavaDAO {

    public static String gostujucePredstave() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
       String gostujucePredstave = ProtocolMessages.GOSTUJUCE_PREDSTAVE_RESPONSE.getMessage();

        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from gostujuca_predstava");
            while (rs.next()) {
                //GostujucaPredstava(rs.getInt("id"), rs.getString("naziv"), rs.getString("opis"), rs.getString("tip"), rs.getString("pisac"), rs.getString("reziser"), rs.getString("glumci"));
                gostujucePredstave+=rs.getInt("id") +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+ rs.getString("naziv")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage() + rs.getString("opis") +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+ rs.getString("tip")
                       +ProtocolMessages.MESSAGE_SEPARATOR.getMessage() + rs.getString("pisac") + ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("reziser") + ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("glumci")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException ex) {
            Logger.getLogger(GostujucaPredstavaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GostujucaPredstavaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GostujucaPredstavaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(gostujucePredstave.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return null;
        }
        else {
            return gostujucePredstave;
        }
    }

    public static void dodajGostujucuPredstavu(GostujucaPredstava gostujucaPredstava){
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodajGostujucuPredstavu(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, gostujucaPredstava.getNaziv());
            callableStatement.setString(2, gostujucaPredstava.getOpis());
            callableStatement.setString(3, gostujucaPredstava.getTip());
            callableStatement.setString(4, gostujucaPredstava.getPisac());
            callableStatement.setString(5, gostujucaPredstava.getReziser());
            callableStatement.setString(6, gostujucaPredstava.getGlumci());
            callableStatement.registerOutParameter(7,Types.VARCHAR);
            callableStatement.registerOutParameter(8,Types.INTEGER);
            callableStatement.executeQuery();

            poruka=callableStatement.getString(7);
            gostujucaPredstava.setId(callableStatement.getInt(8));
            if(poruka!=null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText(poruka);
                alert.showAndWait();
            }
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


    public static void azurirajGostujucuPredstavu(GostujucaPredstava predstava){
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeGostujucePredstave(?,?,?,?,?,?,?)}");
            callableStatement.setInt(1, predstava.getId());
            callableStatement.setString(2, predstava.getNaziv());
            callableStatement.setString(3, predstava.getOpis());
            callableStatement.setString(4, predstava.getTip());
            callableStatement.setString(5, predstava.getPisac());
            callableStatement.setString(6, predstava.getReziser());
            callableStatement.setString(7, predstava.getGlumci());
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
                    //Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static Integer getIdGostujucePredstave(String naziv) {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			connection = ConnectionPool.getInstance().checkOut();
			statement = connection.createStatement();
			rs = statement.executeQuery("select naziv, id from gostujuca_predstava");
			while (rs.next()) {
				if(rs.getString(1).equals(naziv)) {
					return rs.getInt(2);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			
		}
		return -1;
    }
    
}