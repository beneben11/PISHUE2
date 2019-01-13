package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

/**
 * @author 5127797, Ramli, Benedictus William
 * @author 5130292, Fadilah, Verdy Aprian
 * 
 */
public class LaunchServer {

	/**
     * Alle sind Elementen von GUI
     */
    private JFrame frame;
    private JPanel panel;
    private JTextArea server_log;
    private JButton btn_start;
    private JLabel port_label;
    private JTextField port_field;
    private static LaunchServer instance;
    private StringBuilder ServerLog;

    Thread s_thread;
    Server server;

    /**
     * Methode zur Ausfuehrung der GUI
     */
    public LaunchServer(){
        initGui();
        ServerLog = new StringBuilder("");
    }
    /**
     * Methode zum Initialisieren der GUI-Elementen
     */
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
        	/* (non-Javadoc)
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             * Methode zum Starten des Servers durch den Klick
             */
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
        server_log.setBounds(25,100,525,375);
        server_log.setEditable(false);
        panel.add(server_log);

        frame.setSize(new Dimension(610,525));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Server");
        frame.setVisible(true);
    }

    public static LaunchServer getInstance() {
        return instance;
    }

    public StringBuilder getServerLog(){
        return ServerLog;
    }
    /**
     * Methode gibt die Nachricht, wenn Server schon startet, jemand zum Server verbindet oder abtrennt
     */
    public void updateGUI(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                server_log.setText(ServerLog.toString());
            }
        });
    }

    public static void main(String [] args){
        instance = new LaunchServer();
    }

}
