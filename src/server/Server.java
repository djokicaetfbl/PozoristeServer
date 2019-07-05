package server;

import java.net.*;

public class Server {
    // podesavamo port na kom ce server osluskivati zahtjeve od klijenata
    public static final int TCP_PORT = 9000;
    public static void main(String[] args) {
        try {
            // slusaj zahteve na datom portu
            ServerSocket ss = new ServerSocket(TCP_PORT);
            System.out.println("Server running...");
            while (true) {
                // prihvataj klijente
                Socket sock = ss.accept();
                // startuj nit za svakog klijenta
                new ServerThread(sock);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
