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
    private TeilnehmerListe teilnehmerListe;

    public Server(int port){
        this.port = port;
        teilnehmerListe = new TeilnehmerListe();
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
                MessageHandler msg = new MessageHandler(in.readLine());
                if (msg.getCmd().equals("connect")){
                    String username = msg.getMsg();
                    ConnectedClient client = new ConnectedClient(client_socket, username,teilnehmerListe);
                    boolean added = teilnehmerListe.addClient(client);
                    if (added == true){
                        sb.append("New client has connected!"+"\n");
                        Thread t = new Thread(client);
                        t.start();
                        out.print("connect:ok"+"\n");
                        out.flush();
                        teilnehmerListe.sendUpdateClient();
                    }else{
                        sb.append(client_socket.getLocalAddress().getHostName() +" has left."+"\n");
                        client_socket.close();
                    }
                }else if(msg.getCmd().equals("disconnect")){
                    String message = msg.getMsg();
                    if (message.equals(" ")){
                        out.print("disconnect:ok"+"\n");
                        out.flush();
                        client_socket.close();
                    }else{
                        out.print("disconnect:invalid_command"+"\n");
                        out.flush();
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
