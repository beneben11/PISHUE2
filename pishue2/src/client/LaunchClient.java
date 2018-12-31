package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LaunchClient {

    private static LaunchClient instance;
    private JFrame frame;
    private JPanel pane;
    private JLabel port_label;
    private JLabel host_label;
    private JLabel client_list_label;
    private JLabel username_Label;
    private JTextField port_field;
    private JTextField host_field;
    private JTextArea client_list_field;
    private JTextField username_field;
    private JTextField input_field;
    private JTextArea chat_area;
    private JButton btn_connect;
    private JButton btn_disconnect;
    private JButton btn_send;

    Thread thread_client;
    Client client;

    public LaunchClient(){
        initGui();
    }

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

        client_list_field = new JTextArea();
        client_list_field.setBounds(600,295,375,325);
        pane.add(client_list_field);

        btn_connect = new JButton("Connect");
        btn_connect.setBounds(550,130,200,50);
        btn_connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int port = Integer.parseInt(port_field.getText());
                client = new Client(username_field.getText(), host_field.getText(), port);
                thread_client = new Thread(client);
                thread_client.start();
            }
        });
        pane.add(btn_connect);

        btn_disconnect = new JButton("Disconnect");
        btn_disconnect.setBounds(775,130,200,50);
        btn_disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

            }
        });
        pane.add(btn_disconnect);

        chat_area = new JTextArea();
        chat_area.setBounds(50,220,500,400);
        pane.add(chat_area);

        input_field = new JTextField();
        input_field.setBounds(50,660,500,50);
        pane.add(input_field);

        btn_send = new JButton("Send");
        btn_send.setPreferredSize(new Dimension(375,50));
        btn_send.setLocation(600,660);
        btn_send.setBounds(600,660,375,50);
        btn_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

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

    public void updateGui(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chat_area.setText(client.getChatLog());
            }
        });
    }

    public static void main(String[] args){
        instance = new LaunchClient();
    }

}
