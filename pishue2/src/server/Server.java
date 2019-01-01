package server;

import common.MessageHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private int port;
    private ServerSocket ss;
    private Socket client_socket;
    private StringBuilder sb = new StringBuilder("");

    public Server(int port){
        this.port = port;
    }

    public String getServerLog(){
        return sb.toString();
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            sb.append("Server is up and waiting for connection."+"\n");
            LaunchServer.getInstance().updateGUI();
            while(true) {
                client_socket = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(client_socket.getOutputStream()));
                sb.append("New client has connected!"+"\n");
                LaunchServer.getInstance().updateGUI();
                MessageHandler msg = new MessageHandler(in.readLine());
                out.print("connect:ok"+"\n");
                out.flush();
                String username = msg.getMsg();
                Teilnehmer teilnehmer = new Teilnehmer(client_socket,username);
                Thread t = new Thread(teilnehmer);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
