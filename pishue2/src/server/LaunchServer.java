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
        port_label.setBounds(25,25,50,50);
        panel.add(port_label);

        port_field = new JTextField("1996");
        port_field.setBounds(95,25,100, 50);
        panel.add(port_field);

        btn_start = new JButton("Start");
        btn_start.setBounds(215,25,155,50);
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

        btn_stop = new JButton("Stop");
        btn_stop.setBounds(390,25,155,50);
        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(ConnectedClient client : server.getTeilnehmerListe().getClient_list()){
                    try {
                        PrintWriter to_client = new PrintWriter(new OutputStreamWriter(client.getClient_socket().getOutputStream()));
                        to_client.print("disconnect:invalid_command"+"\n");
                        to_client.flush();
                        client.getClient_socket().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    server.getSs().close();
                } catch (IOException e1) {
                    if(!server.getSs().isClosed()) {
                        e1.printStackTrace();
                    }
                }
                s_thread.interrupt();
                server_log.setText(server_log.getText()+"Server stopped."+"\n");
            }
        });
        panel.add(btn_stop);

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
