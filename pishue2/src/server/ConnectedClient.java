package server;
/**
 * @author 5127797, Ramli, Benedictus William
 * @author 5130292, Fadilah, Verdy Aprian
 * 
 */
import common.MessageHandler;

import java.io.*;
import java.net.Socket;

public class ConnectedClient implements Runnable {

    private Socket client_socket;
    private String username;
    private TeilnehmerListe teilnehmerListe;

    /**
     * @param s
     * @param username
     * @param teilnehmerListe
     * parameterisiert Constructor
     */
    public ConnectedClient(Socket s, String username, TeilnehmerListe teilnehmerListe){
        this.username = username;
        this.client_socket = s;
        this.teilnehmerListe = teilnehmerListe;
    }

    public Socket getClient_socket() {
        return client_socket;
    }

    public String getUsername() {
        return username;
    }

    public TeilnehmerListe getTeilnehmerListe(){
        return teilnehmerListe;
    }
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     * Diese Methode bedient den Client waehrend der Verbindung mit dem Server
     */
    @Override
    public void run() {
        try {
            BufferedReader from_client = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            PrintWriter to_client;
            while(!client_socket.isClosed()){
                String inc_msg = from_client.readLine();
                String sender = this.getUsername();
                if(!inc_msg.equals("")) {
                    MessageHandler msg = new MessageHandler(inc_msg);
                    if(msg.getCmd().equals("message")){
                        for(ConnectedClient clients: teilnehmerListe.getClient_list()){
                            to_client = new PrintWriter(new OutputStreamWriter(clients.getClient_socket().getOutputStream()));
                            String message = msg.getMsg();
                            to_client.print(msg.getCmd() + ":"+ sender +" : " + message + "\n");
                            to_client.flush();
                            
                        }
                    }
                    else if(msg.getCmd().equals("disconnect")){
                        String username = getUsername();
                        PrintWriter to_this_client = new PrintWriter(new OutputStreamWriter(getClient_socket().getOutputStream()));
                        String message = "disconnect:ok"+"\n";
                        to_this_client.print(message);
                        to_this_client.flush();
                        teilnehmerListe.deleteClient(username);
                        teilnehmerListe.sendUpdateClient();
                        client_socket.close();
                        to_this_client.close();
                        for(ConnectedClient clients: teilnehmerListe.getClient_list()){
                            to_client = new PrintWriter(new OutputStreamWriter(clients.getClient_socket().getOutputStream()));
                            to_client.print("message:"+username+" has left." + "\n");
                            to_client.flush();
                            
                        }
                    }
                }
            }
            from_client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
