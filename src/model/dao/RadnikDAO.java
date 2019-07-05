package model.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dto.Biletar;
import model.dto.Radnik;

public class RadnikDAO {



    public static Radnik pretraziRadnika(String JMB) {

        System.out.println("JMB: " + JMB);
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pretrazi_radnika(?)}");
            callableStatement.setString(1, JMB);
            rs = callableStatement.executeQuery();
            if (rs.next()) {
                System.out.println("uso");
                //return new Zaposleni
                //System.out.println("perp" + rs.getString("jmb")+ rs.getString("ime")+rs.getString("prezime")
                //+rs.getString("adresa")+rs.getString("brojtelefona")+rs.getInt("postanskibroj")+rs.getBigDecimal("plata"));

                //return new Radnik(rs.getString("ime"),rs.getString("prezime"),rs.getString("opisPosla"),rs.getString("jmb")
                //,rs.getBoolean("statusRadnika"),rs.getString("kontakt"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(RadnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
        }
        return null;
    }

}
