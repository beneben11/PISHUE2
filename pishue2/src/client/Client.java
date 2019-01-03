package client;

import common.MessageHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {

    private Client instance;
    private String host;
    private int port;
    private String username;
    private Socket s;
    private StringBuilder chatLog = new StringBuilder("");
    private ArrayList<String> member_list = new ArrayList<>();
    private boolean newMessage = false;
    private boolean newClient = false;

    public Client() {
        this.username = "Client";
        this.host = "127.0.0.1";
        this.port = 1996;
    }

    public Client(String username, String host, int port) {
        this.username = username;
        this.host = host;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getChatLog() {
        return chatLog.toString();
    }

    public Socket getS() {
        return s;
    }

    public synchronized boolean getNewMessage() {
        return newMessage;
    }

    public ArrayList getMemberList() {
        return member_list;
    }

    public Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    public void sendMessage(String msg) {
        try {
            if (s != null) {
                if (s.isConnected()) {
                    PrintWriter print = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
                    print.print(msg + "\n");
                    print.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            s = new Socket(host, port);
            BufferedReader socket_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String readIncomingMessage = "";
            sendMessage("connect:" + username);
            while (!s.isClosed()) {
                readIncomingMessage = socket_in.readLine();
                if (readIncomingMessage != null) {
                    MessageHandler incoming_msg = new MessageHandler(readIncomingMessage);
                    String cmd = incoming_msg.getCmd();
                    String msg = incoming_msg.getMsg();
                    System.out.println(cmd+":"+msg);
                    newMessage = true;
                    switch (cmd) {
                        case "connect":
                            if (msg.equals("ok")) {
                                chatLog.append("Connection to the server is succesfully established"+"\n");
                            }
                            break;
                        case "refused":
                            switch (msg) {
                                case "too_many_users":
                                    chatLog.append("Too many users connected to the server." + "\n");
                                    break;
                                case "name_in_use":
                                    chatLog.append("Username is already taken. Please choose another username" + "\n");
                                    break;
                                case "invalid_name":
                                    chatLog.append("Username contains : or has more than 30 words." + "\n");
                                    break;
                            }
                            break;
                        case "disconnect":
                            switch (msg) {
                                case "ok":
                                    chatLog.append("Disconnected from the server" + "\n");
                                    member_list.clear();
                                    s.close();
                                    break;
                                case "invalid_command":
                                    chatLog.append("Error occured when disconnecting from the server." + "\n");
                                    member_list.clear();
                                    s.close();
                                    break;
                            }
                            break;
                        case "namelist":
                            member_list.clear();
                            String[] users = incoming_msg.getUsers();
                            for (String user : users) {
                                member_list.add(user);
                            }
                            newClient = true;
                            break;
                        case "message":
                            chatLog.append(msg + "\n");
                            break;
                    }
                    if (newMessage) {
                        LaunchClient.getInstance().updateChat();
                        newMessage = false;
                    }
                    if (newClient){
                        LaunchClient.getInstance().updateClient();
                        newClient = false;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
