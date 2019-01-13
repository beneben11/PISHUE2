package server;

import common.MessageHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 5127797, Ramli, Benedictus William
 * @author 5130292, Fadilah, Verdy Aprian
 */

public class Server implements Runnable {

    private int port;
    private ServerSocket ss;
    private Socket client_socket;
    private TeilnehmerListe teilnehmerListe;

    public Server(int port) {
        this.port = port;
        teilnehmerListe = new TeilnehmerListe();
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     * Diese Methode bestaetigt die Moeglichkeit zum Verbinden
     */
    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            LaunchServer.getInstance().getServerLog().append("Server is up and waiting for connection." + "\n");
            LaunchServer.getInstance().updateGUI();
            while (true) {
                client_socket = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(client_socket.getOutputStream()));
                MessageHandler msg = new MessageHandler(in.readLine());
                if (msg.getCmd().equals("connect")) {
                    String username = msg.getMsg();
                    ConnectedClient client = new ConnectedClient(client_socket, username, teilnehmerListe);
                    boolean added = teilnehmerListe.addClient(client);
                    if (added == true) {
                        LaunchServer.getInstance().getServerLog().append(username+" has connected to the server." + "\n");
                        Thread t = new Thread(client);
                        t.start();
                        out.print("connect:ok" + "\n");
                        out.flush();
                        teilnehmerListe.sendUpdateClient();
                    } else {
                        LaunchServer.getInstance().getServerLog().append(username + " has left." + "\n");
                        client_socket.close();
                    }
                }
                LaunchServer.getInstance().updateGUI();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
