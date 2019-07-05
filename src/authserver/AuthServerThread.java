package authserver;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.dao.AdministratorDAO;
import model.dao.ConnectionPool;
import util.ProtocolMessages;

public class AuthServerThread extends Thread {

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;

    public AuthServerThread(Socket sock) {
        this.sock = sock;
        try {
            // inicijalizuj ulazni stream
            in = new DataInputStream(
                    sock.getInputStream());
            // inicijalizuj izlazni stream
            out = new DataOutputStream(
                    sock.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //zahtjev u formatu AUTH#username#password
            String request = in.readUTF();
            System.out.println(request);
            String[] params = request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
            if (request.startsWith(ProtocolMessages.AUTH_REQUEST.getMessage()) && params.length == 3) {
                String username = params[1];
                String password = params[2];
                try {
                	String res=postojiUBazi(username, password);
                    if (res!=null) {
                    	//String id=AdministratorDAO.vratiId(ProtocolMessages.AUTH_REQUEST.getMessage().split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage())[3]);
                        out.writeUTF(ProtocolMessages.OK.getMessage()+res);
                    } else {
                        out.writeUTF(ProtocolMessages.NOT_OK.getMessage());
                    }
                } catch (NullPointerException ex) {
                    System.err.println("Missing password for username " + username);
                }
            } else {
                out.writeUTF(ProtocolMessages.INVALID_REQUEST.getMessage());
            }
            in.close();
            out.close();
            sock.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String postojiUBazi(String username, String passwordHash) {
        boolean postoji = false;
        String tipKorisnika;
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call provjeraLogovanja(?,?,?)}");
            callableStatement.setString(1, username);
            callableStatement.setString(2, passwordHash);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.executeQuery();
            postoji = callableStatement.getBoolean(3);
            if (postoji) {
                callableStatement = connection.prepareCall("{call provjeraLozinkeIKorisnickogImena(?,?)}");
                callableStatement.setString(1, username);
                callableStatement.setString(2, passwordHash);

                resultSet = callableStatement.executeQuery();
                if (resultSet.next()) {
                    tipKorisnika = resultSet.getString("tipKorisnika");
                    return tipKorisnika;
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionPool.getInstance().checkIn(connection);
        }
        return "";
    }
}
