package model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool {

    private String jdbcURL;
    private String username;
    private String password;
    private int preconnectCount;
    private int maxIdleConnections;
    private int maxConnections;

    private int connectCount;
    private List<Connection> usedConnections;
    private List<Connection> freeConnections;

    private static ConnectionPool instance;

    private ConnectionPool() {
        readConfiguration();
        try {
            this.freeConnections = new ArrayList<Connection>();
            this.usedConnections = new ArrayList<Connection>();

            for (int i = 0; i < this.preconnectCount; i++) {
                Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                freeConnections.add(connection);
            }
            connectCount = preconnectCount;
        } catch (Exception e) {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void readConfiguration() {
        try (FileInputStream fileInputStream = new FileInputStream("ConnectionPool.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);

            jdbcURL = properties.getProperty("jdbcURL");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            preconnectCount = Integer.parseInt(properties.getProperty("preconnectCount"));
            maxIdleConnections = Integer.parseInt(properties.getProperty("maxIdleConnections"));
            maxConnections = Integer.parseInt(properties.getProperty("maxConnections"));
        } catch (IOException ex) {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection checkOut() throws SQLException {
        Connection connection = null;
        if (freeConnections.size() > 0) {
            connection = freeConnections.remove(0);
            usedConnections.add(connection);
        } else {
            if (connectCount < maxConnections) {
                connection = DriverManager.getConnection(jdbcURL, username, password);
                usedConnections.add(connection);
                connectCount++;
            } else {
                try {
                    wait();
                    connection = usedConnections.remove(0);
                    usedConnections.add(connection);
                } catch (Exception e) {
                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return connection;
    }

    public synchronized void checkIn(Connection connection) {
        if (connection == null) {
            return;
        }
        if (usedConnections.remove(connection)) {
            freeConnections.add(connection);
            while (freeConnections.size() > maxIdleConnections) {
                int lastOne = freeConnections.size() - 1;
                Connection connectionForClose = freeConnections.remove(lastOne);
                try {
                    connectionForClose.close();
                } catch (Exception e) {
                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            notify();
        }
    }

    public int getNumberOfActiveConnection() {
        return this.connectCount;
    }

}