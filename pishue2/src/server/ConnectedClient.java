package server;

import common.MessageHandler;

import java.io.*;
import java.net.Socket;

public class ConnectedClient implements Runnable {

    private Socket client_socket;
    private String username;
    private TeilnehmerListe teilnehmerListe;

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

    @Override
    public void run() {
        try {
            BufferedReader from_client = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            PrintWriter to_client;
            while(client_socket.isConnected()){
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
