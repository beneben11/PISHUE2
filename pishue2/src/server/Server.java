package server;

import common.MessageHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

	private int port;
    private ServerSocket ss;
    private Socket client_socket;

    public Server(){
        this.port = 1996;
    }
    
    public Server(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
			ExecutorService exec = Executors.newFixedThreadPool(3);
            ss = new ServerSocket(port);
            System.out.println("Server is up");
            client_socket = ss.accept();
            BufferedReader from_client = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            PrintWriter out = new PrintWriter(client_socket.getOutputStream());
            while(!ss.isClosed()) {
                String incoming_msg = from_client.readLine();
                System.out.print(incoming_msg + "\n");
                MessageHandler msg = new MessageHandler(incoming_msg);
                String cmd = msg.getCmd();
                String user = msg.getMsg();
                String anwort = (cmd + ":ok" + "\n");
                out.print(anwort);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setPort(int port) {
        
        this.port = port;
    }
    public synchronized ServerSocket getServerSocket() {
        
        return ss;
    }
}
