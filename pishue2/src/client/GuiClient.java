package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GuiClient {

    private Pane mainPane;
    private Label port_label;
    private Label host_label;
    private Label client_list_label;
    private Label username_label;
    private TextField port_field;
    private TextField host_field;
    private TextField client_list_field;
    private TextField username_field;
    private TextField input_field;
    private TextArea chat_area;
    private Button button_connect;
    private Button button_disconnect;
    private Button button_send;

    Thread thread_client;
    Client client;
    ArrayList<String> member_list = new ArrayList<>();

    public GuiClient(){
        mainPane = new Pane();
        mainPane.setPrefSize(1025, 750);

        host_label = new Label("Host");
        host_label.setPrefSize(100,50);
        host_label.setLayoutX(50);
        host_label.setLayoutY(40);

        port_label = new Label("Port");
        port_label.setPrefSize(100,50);
        port_label.setLayoutX(550);
        port_label.setLayoutY(40);

        client_list_label = new Label("List of connected Clients");
        client_list_label.setPrefSize(375,50);
        client_list_label.setLayoutX(600);
        client_list_label.setLayoutY(220);
        client_list_label.setStyle("-fx-alignment: center");

        username_label = new Label("Username");
        username_label.setPrefSize(100,50);
        username_label.setLayoutX(50);
        username_label.setLayoutY(130);

        port_field = new TextField("1996");
        port_field.setPrefSize(325,50);
        port_field.setLayoutX(650);
        port_field.setLayoutY(40);

        host_field = new TextField("127.0.0.1");
        host_field.setPrefSize(325,50);
        host_field.setLayoutX(150);
        host_field.setLayoutY(40);

        username_field = new TextField();
        username_field.setPromptText("Enter Username");
        username_field.setPrefSize(325,50);
        username_field.setLayoutX(150);
        username_field.setLayoutY(130);

        client_list_field = new TextField();
        client_list_field.setPrefSize(375, 325);
        client_list_field.setLayoutX(600);
        client_list_field.setLayoutY(295);

        button_connect = new Button("Connect");
        button_connect.setPrefSize(200,50);
        button_connect.setLayoutX(550);
        button_connect.setLayoutY(130);
        button_connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = username_field.getText();
                int port = Integer.parseInt(port_field.getText());
                client = new Client(username,host_field.getText(), port);
                thread_client = new Thread(client);
                thread_client.start();
            }
        });

        button_disconnect = new Button("Disconnect");
        button_disconnect.setPrefSize(200,50);
        button_disconnect.setLayoutX(775);
        button_disconnect.setLayoutY(130);

        chat_area = new TextArea();
        chat_area.setEditable(false);
        chat_area.setPrefSize(500,400);
        chat_area.setLayoutX(50);
        chat_area.setLayoutY(220);

        input_field = new TextField();
        input_field.setPromptText("Enter your message here");
        input_field.setPrefSize(500, 50);
        input_field.setLayoutX(50);
        input_field.setLayoutY(660);

        button_send = new Button("Send");
        button_send.setPrefSize(375,50);
        button_send.setLayoutX(600);
        button_send.setLayoutY(660);
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = input_field.getText();
                client.sendMessage("message:"+text);
            }
        });

        mainPane.getChildren().addAll(host_label,port_label,client_list_label,username_label,port_field,host_field,username_field,client_list_field,button_connect,button_disconnect,button_send,
                chat_area,input_field);

    }

    public Parent getView(){
        return mainPane;
    }

    public void setText(String msg) {
        System.out.println(msg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chat_area.setText(msg);
            }
        });
    }
}
