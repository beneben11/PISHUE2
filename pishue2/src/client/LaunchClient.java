package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author 5127797, Ramli, Benedictus William
 * @author 5130292, Fadilah, Verdy Aprian
 * Class zur Ausfuehrung des Client-Fensters
 */

public class LaunchClient {

	
    /**
     * Zustand von Objekt
     */
    private static LaunchClient instance;
    /**
     * Alle sind Elementen von GUI
     */
    private JFrame frame;
    private JPanel pane;
    private JLabel port_label;
    private JLabel host_label;
    private JLabel client_list_label;
    private JLabel username_Label;
    private JTextField port_field;
    private JTextField host_field;
    private JList client_list_field;
    private JTextField username_field;
    private JTextField input_field;
    private JTextArea chat_area;
    private JButton btn_connect;
    private JButton btn_disconnect;
    private JButton btn_send;
    private DefaultListModel<String> list_model;

    Thread thread_client;
    Client client;

    /**
     * Methode zur Ausfuehrung der GUI
     */
    public LaunchClient(){
    	
        initGui();
    }

    /**
     * Methode zum Initialisieren der GUI-Elementen
     */
  
    public void initGui(){
    	
        frame = new JFrame();

        pane = new JPanel();
        frame.getContentPane().add(pane);
        pane.setLayout(null);

        host_label = new JLabel("Host");
        host_label.setBounds(50,40,100,50);
        pane.add(host_label);

        port_label = new JLabel("Port");
        port_label.setBounds(550,40,100,50);
        pane.add(port_label);

        client_list_label = new JLabel("List of connected Clients",SwingConstants.CENTER);
        client_list_label.setBounds(600,220,375,50);
        pane.add(client_list_label);

        username_Label = new JLabel("Username");
        username_Label.setBounds(50,130,100,50);
        pane.add(username_Label);

        port_field = new JTextField("1996");
        port_field.setBounds(650,40,325,50);
        pane.add(port_field);

        host_field = new JTextField("127.0.0.1");
        host_field.setBounds(150,40,325,50);
        pane.add(host_field);

        username_field = new JTextField();
        username_field.setBounds(150,130,325,50);
        pane.add(username_field);

        list_model = new DefaultListModel<>();
        client_list_field = new JList(list_model);
        client_list_field.setBounds(600,295,375,325);
        pane.add(client_list_field);

        
        btn_connect = new JButton("Connect");
        btn_connect.setBounds(550,130,200,50);
        btn_connect.addActionListener(new ActionListener() {
        	
            /* (non-Javadoc)
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * Methode zum Verbinden zum Server durch den Klick
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(!port_field.getText().equals("") && !host_field.getText().equals("") && !username_field.getText().equals("")) {
                    int port = Integer.parseInt(port_field.getText());
                    client = new Client(username_field.getText(), host_field.getText(), port);
                    thread_client = new Thread(client);
                    thread_client.start();
                }else{
                    JOptionPane.showMessageDialog(null, "Port, Host or Username is empty.");
                }

            }
        });
        pane.add(btn_connect);

        btn_disconnect = new JButton("Disconnect");
        btn_disconnect.setEnabled(false);
        btn_disconnect.setBounds(775,130,200,50);
        btn_disconnect.addActionListener(new ActionListener() {
            /* (non-Javadoc)
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * Methode zum Abtrennen von Server durch den Klick
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                client.sendMessage("disconnect: "+"\n");
                btn_connect.setEnabled(true);
                username_field.setEditable(true);
                port_field.setEditable(true);
                host_field.setEditable(true);
                btn_disconnect.setEnabled(false);
            }
        });
        pane.add(btn_disconnect);

        chat_area = new JTextArea();
        chat_area.setBounds(50,220,500,400);
        chat_area.setEditable(false);
        pane.add(chat_area);

        input_field = new JTextField();
        input_field.setBounds(50,660,500,50);
        pane.add(input_field);

        btn_send = new JButton("Send");
        btn_send.setPreferredSize(new Dimension(375,50));
        btn_send.setLocation(600,660);
        btn_send.setBounds(600,660,375,50);
        btn_send.addActionListener(new ActionListener() {
            /* (non-Javadoc)
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * Methode zum Senden des Chats von Client
             */
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	
                String msg = "message:"+input_field.getText();
                client.sendMessage(msg);
                input_field.setText("");
            }
        });
        pane.add(btn_send);

        frame.setSize(new Dimension(1025,775));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("client");
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static LaunchClient getInstance(){
        return instance;
    }

    
    /**
     * Zum Aktualisieren des Chat-Fensters
     */
    public void updateChat(){
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chat_area.setText(client.getChatLog());
            }
        });
    }

    /**
     * Zum Aktualisieren des Fensters von verbundenen Clients
     */
    public void updateClient(){
        list_model.clear();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> member = new ArrayList<>();
                member = client.getMemberList();
                for(int i = 0; i<member.size(); i++){
                    list_model.add(i,member.get(i));
                }
            }
        });
    }

    /**
     * Zum Setzen von mehreren GUI-Elementen w�hrend der Verbindung von Client
     */
    public void setGUIWhenConnected(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                btn_connect.setEnabled(false);
                username_field.setEditable(false);
                port_field.setEditable(false);
                host_field.setEditable(false);
                btn_disconnect.setEnabled(true);
            }
        });
    }

    public static void main(String[] args){
        instance = new LaunchClient();
    }

}
