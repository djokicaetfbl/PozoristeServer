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
import model.dto.Predstava;
import util.ProtocolMessages;

public class PredstavaDAO {

    public static String predstave() {
        String predstave = ProtocolMessages.PREDSTAVE_RESPONSE.getMessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  predstava";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Predstava(resultSet.getString("naziv"), resultSet.getString("opis"), resultSet.getString("tip"));
                //predstava.setId(resultSet.getInt("id"));
                predstave+=resultSet.getInt("id")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getString("naziv") +ProtocolMessages.MESSAGE_SEPARATOR.getMessage() + resultSet.getString("opis")
                        +ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+ resultSet.getString("tip")+ProtocolMessages.LINE_SEPARATOR.getMessage();
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
        if(predstave.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage()).length<3){
            return null;
        }
        else {
            return predstave;
        }
    }

    public static void dodajPredstavu(Predstava predstava) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodajPredstavu(?,?,?,?,?)}");
            callableStatement.setString(1, predstava.getNaziv());
            callableStatement.setString(2, predstava.getOpis());
            callableStatement.setString(3, predstava.getTip());
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.registerOutParameter(5, Types.INTEGER);
            callableStatement.executeQuery();

            predstava.setId(callableStatement.getInt(5));
            poruka = callableStatement.getString(4);
            if (poruka != null) {
                System.out.println("Predstava vec postoji u bazi!");
            }
        } catch (SQLException ex) {
          //  Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                 //   Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void azurirajPredstavu(Predstava predstava) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjePredstave(?,?,?,?)}");
            callableStatement.setInt(1, predstava.getId());
            callableStatement.setString(2, predstava.getNaziv());
            callableStatement.setString(3, predstava.getOpis());
            callableStatement.setString(4, predstava.getTip());
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
/*
    public static void UkloniPredstavu(Predstava predstava) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            String query = "delete from predstava  where id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, predstava.getId());
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
*/
    public static Integer getIdPredstave(String naziv) {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			connection = ConnectionPool.getInstance().checkOut();
			statement = connection.createStatement();
			rs = statement.executeQuery("select naziv, id from predstava");
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