package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchServer {

    private JFrame frame;
    private JPanel panel;
    private JTextArea server_log;
    private JButton btn_start;
    private JLabel port_label;
    private JLabel connected_client;
    private JTextArea list_client;
    private JTextField port_field;
    private static LaunchServer instance;

    Thread s_thread;
    Server server;

    public LaunchServer(){
        initGui();
    }

    public void initGui(){
        frame = new JFrame();

        panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        port_label = new JLabel("Port");
        port_label.setBounds(25,25,100,50);
        panel.add(port_label);

        port_field = new JTextField("1996");
        port_field.setBounds(150,25,200, 50);
        panel.add(port_field);

        btn_start = new JButton("Start");
        btn_start.setBounds(375,25,200,50);
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(port_field.getText());
                server = new Server(port);
                s_thread = new Thread(server);
                s_thread.start();
            }
        });
        panel.add(btn_start);

        server_log = new JTextArea();
        server_log.setBounds(25,100,262,375);
        server_log.setEditable(false);
        panel.add(server_log);

        connected_client = new JLabel("Connected Clients",SwingConstants.CENTER);
        connected_client.setBounds(313,100,262,75);
        panel.add(connected_client);

        list_client = new JTextArea();
        list_client.setBounds(313,200,262,275);
        panel.add(list_client);

        frame.setSize(new Dimension(610,525));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Server");
        frame.setVisible(true);
    }

    public static LaunchServer getInstance() {
        return instance;
    }

    public void updateGUI(){
        server_log.setText(server.getServerLog());
    }

    public static void main(String [] args){
        instance = new LaunchServer();
    }

}
