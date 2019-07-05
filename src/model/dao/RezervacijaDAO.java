package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import model.dto.Rezervacija;
//import net.etfbl.is.pozoriste.controller.PregledKarataController;

import model.dto.RezervisanoSjediste;
import util.ProtocolMessages;

public class RezervacijaDAO {

    public static String rezervacije(Date termin, Integer idScene) {
        String rezervacije = ProtocolMessages.REZERVACIJE_RESPONSE.getMessage();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledRezervacija(?,?)}");
            callableStatement.setDate(1, termin);
            callableStatement.setInt(2, idScene);

            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(resultSet.getDate("termin"));
                //Rezervacija(Integer id, String ime, Date termin, Integer idScene)
                rezervacije+=resultSet.getInt("id")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getString("ime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()
                        +strDate+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+resultSet.getInt("idScene")+ProtocolMessages.LINE_SEPARATOR.getMessage();
            }

        } catch (SQLException sql) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return rezervacije;
    }

    public static void addRezervacija(Rezervacija rezervacija) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Rezervacija rezervacijaDodata = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRezervacije(?,?,?,?,?)}");
            callableStatement.setInt(1, rezervacija.getId());
            callableStatement.setString(2, rezervacija.getIme());
            callableStatement.setDate(3, rezervacija.getTermin());
            callableStatement.setInt(4, rezervacija.getIdScene());
            callableStatement.registerOutParameter(5, Types.INTEGER);

            if (callableStatement.executeUpdate() == 0) {
                rezervacija.setId(callableStatement.getInt(5));
                rezervacijaDodata = rezervacija;
//                return rezervacijaDodata;
            }
        } catch (SQLException sql) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
//        return rezervacijaDodata;
    }



    public static boolean obrisiRezervaciju(final Rezervacija rezervacija) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call otkazivanjeRezervacije(?)}");
            callableStatement.setInt(1, rezervacija.getId());

            List<RezervisanoSjediste> rezervisanaSjedistaZaBrisanje = RezervisanoSjedisteDAO.sjedistaDAO(rezervacija.getTermin(), rezervacija.getIdScene()).stream().filter(e -> e.getIdRezervacije() == rezervacija.getId()).collect(Collectors.toList());
            rezervisanaSjedistaZaBrisanje.forEach(e -> {
                RezervisanoSjedisteDAO.obrisiRezervisanoSjediste(e);
            });

            callableStatement.executeQuery();

        } catch (SQLException sql) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return true;
    }

}