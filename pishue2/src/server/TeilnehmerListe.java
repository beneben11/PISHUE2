package server;

import client.LaunchClient;
import common.MessageHandler;

import java.io.*;
import java.net.Socket;
import java.util.*;
/**
 * @author 5127797, Ramli, Benedictus William
 * @author 5130292, Fadilah, Verdy Aprian
 */
public class TeilnehmerListe {

    private ArrayList<ConnectedClient> client_list;

    public TeilnehmerListe(){
        client_list = new ArrayList<>();
    }

    /**
     * Zum Hinzufuegen der Clients
     * @param client
     * @return
     * @throws IOException gibt Fehlermeldung, wenn es zuviel Clients gibt, Name nicht mehr verfügbar ist,
     * Name zu lang ist, oder Name unzulaessige Zeichen benutzt. 
     */
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

    /**
     * ueberprueft, ob der Name zulaessig und nicht zu lang ist.
     * @param username
     * @return
     */
    public synchronized boolean checkName(String username){
        boolean correct = false;
        if(username.length() <= 30 && !username.contains(":")){
            correct = true;
            return  correct;
        }
        return correct;
    }

    /**
     * ueberprueft, ob der Name noch verfuegbar ist.
     * @param username
     * @return
     */
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

    /**
     * Zum Erhalten des Index von Client in Array von Client
     * @param username
     * @return
     */
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

    /**
     * loescht den Name des Clients nach der Abtrennung
     * @param username
     */
    public synchronized void deleteClient(String username){
        int index = getIndex(username);
        client_list.remove(index);
        LaunchServer.getInstance().getServerLog().append(username +" has left."+"\n");
        LaunchServer.getInstance().updateGUI();
    }

    /**
     * @return gibt die Name der Clients zurueck
     */
    public synchronized String getMember(){
        String name = "";
        for(ConnectedClient clients : client_list){
            name = name +":"+clients.getUsername();
        }
        return name;
    }

    /**
     * Zum Aktualisieren der Client-List
     * @throws IOException
     */
    public void sendUpdateClient() throws IOException{
        String msg = "namelist"+getMember()+"\n";
        for(ConnectedClient client : client_list){
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getClient_socket().getOutputStream()));
            out.print(msg);
            out.flush();
        }

    }
}
