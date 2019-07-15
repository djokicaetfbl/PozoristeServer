package authserver;

import authserver.AuthServerThread;

import java.net.*;

public class AuthServer {

	// podesavamo port na kom ce server osluskivati zahtjeve od klijenata
	public static final int TCP_PORT = 9001;


	public static void main(String[] args) {
		try {
			// slusaj zahteve na datom portu
			ServerSocket ss = new ServerSocket(TCP_PORT);
			System.out.println("Auth Server running...");
			while (true) {
				// prihvataj klijente
				Socket sock = ss.accept();
				// startuj nit za svakog klijenta
				new AuthServerThread(sock).start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
