package server;

import java.net.UnknownHostException;
import server.Server;
import server.ServerGUI;

public class LaunchServer {

public static void main(String[] args) throws UnknownHostException {
        
        ServerGUI ui = new ServerGUI(new Server());
        new Thread(ui).start();
    } 
}
