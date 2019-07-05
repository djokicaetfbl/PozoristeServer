package model.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dto.Biletar;
import model.dto.Radnik;
import util.ProtocolMessages;

public class BiletarDAO {

    public static void dodajBiletara(Biletar biletar) {
        System.out.println("DODAVANJE BILETARA : : : "+biletar.getHash());
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeBiletara(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, biletar.getIme());
            callableStatement.setString(2, biletar.getPrezime());
            callableStatement.setString(3, biletar.getJmb());
            callableStatement.setString(4, biletar.getKontakt());
            callableStatement.setString(5, biletar.getKorisnickoIme());
            callableStatement.setString(6, biletar.getHash());
            callableStatement.setString(7, biletar.getTipRadnika());
            callableStatement.registerOutParameter(8, Types.INTEGER);

            callableStatement.executeQuery();

            biletar.setIdRadnika(callableStatement.getInt(8));
            System.out.println("BILETAR ID: "+biletar.getIdRadnika());
        } catch (SQLException ex) {
            Logger.getLogger(BiletarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BiletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    /*
        private static String hashSHA256(String value) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(RadnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            String hash = bytesToHex(encodedhash);
            return hash;
        }
        private static String bytesToHex(byte[] hash) {
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
    */
    
    public static String ubaciUTabeluRadnik() {
    	String response=ProtocolMessages.UBACI_U_TABELU_RADNIK_BILETAR_RESPONSE.getMessage();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Biletar biletar;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from biltetari_info");
            while (rs.next()) {
            	String status=rs.getBoolean("StatusRadnika")==true?"true":"false";
            	response+=rs.getString("Ime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("Prezime")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+
            			rs.getString("JMB")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+status+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("Kontakt")+
            			ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("KorisnickoIme")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("HashLozinke")+
            			ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+rs.getString("TipKorisnika")+ProtocolMessages.MESSAGE_SEPARATOR.getMessage()+
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

    public static void izmjeniBiletara(Biletar biletar) {
        System.out.println("IZMJENA BILETARA : : : "+biletar.getHash());
        System.out.println("BILETAR Status : "+biletar.isStatusRadnika());
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeRadnikaKojiKoristiSistem(?,?,?,?,?,?,?)}");

            callableStatement.setString(1, biletar.getIme());
            callableStatement.setString(2, biletar.getPrezime());
            callableStatement.setString(3, biletar.getJmb());
            callableStatement.setInt(4, biletar.getIdRadnika());
            callableStatement.setBoolean(5, biletar.isStatusRadnika());
            callableStatement.setString(6, biletar.getKorisnickoIme());
            callableStatement.setString(7, biletar.getHash());

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