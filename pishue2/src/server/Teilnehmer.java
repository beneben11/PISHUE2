package server;

import common.MessageHandler;

import java.io.*;
import java.net.Socket;

public class Teilnehmer implements Runnable{

    private Socket client_socket;
    private String username;

    public Teilnehmer(Socket s, String username){
        this.username = username;
        this.client_socket = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader from_client = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            PrintWriter to_client = new PrintWriter(new OutputStreamWriter(client_socket.getOutputStream()));
            while(client_socket.isConnected()){
                String inc_msg = from_client.readLine();
                if(!inc_msg.equals("")) {
                    MessageHandler msg = new MessageHandler(inc_msg);
                    String cmd = msg.getCmd();
                    String message = msg.getMsg();
                    to_client.print(cmd + ":" + message + "\n");
                    to_client.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
