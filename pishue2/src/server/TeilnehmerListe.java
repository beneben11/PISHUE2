package server;

import common.MessageHandler;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class TeilnehmerListe {

    private ArrayList<ConnectedClient> client_list;
    private String member;

    public TeilnehmerListe(){
        client_list = new ArrayList<>();
    }

    public synchronized boolean addClient(ConnectedClient client) throws IOException {
        boolean added = false;
        String username = client.getUsername();
        Socket client_socket = client.getClient_socket();
        if(!checkDuplicate(username) && clientCount()<3&& checkName(username)){
            client_list.add(client);
            added = true;
            return added;
        }else{
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client_socket.getOutputStream()));
            if(checkDuplicate(username)){
                String error = "refused:name_in_use"+"\n";
                out.print(error);
                out.flush();
            }else if(clientCount()>=3){
                String error = "refused:too_many_users"+"\n";
                out.print(error);
                out.flush();
            }else{
                String error = "refused:invalid_name"+"\n";
                out.print(error);
                out.flush();
            }
            out.close();
        }
        return added;
    }

    public synchronized int clientCount(){
        return client_list.size();
    }

    public synchronized boolean checkName(String username){
        boolean correct = false;
        if(username.length() <= 30 && !username.contains(":")){
            correct = true;
            return  correct;
        }
        return correct;
    }

    public synchronized boolean checkDuplicate(String username){
        boolean taken = false;
        for(int i = 0; i < clientCount(); i++){
            if(client_list.get(i).getUsername().equals(username)){
                taken = true;
                return taken;
            }else{
                taken = false;
            }
        }
        return taken;
    }

    public synchronized int getIndex(String username){
        int index = 0;
        for(int i = 0; i < clientCount(); i++){
            if(client_list.get(i).getUsername().equals(username)){
                index = i;
            }
        }
        return index;
    }

    public ArrayList<ConnectedClient> getClient_list() {
        return client_list;
    }

    public synchronized void deleteClient(String username){
        int index = getIndex(username);
        client_list.remove(index);
    }

    public synchronized String getMember(){
        String name = "";
        System.out.println(client_list.size());
        for(ConnectedClient clients : client_list){
            name = name +":"+clients.getUsername();
        }
        System.out.println(name);
        return name;
    }

    public void sendUpdateClient() throws IOException{
        String msg = "namelist"+getMember()+"\n";
        for(ConnectedClient client : client_list){
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getClient_socket().getOutputStream()));
            out.print(msg);
            out.flush();
        }

    }
}
