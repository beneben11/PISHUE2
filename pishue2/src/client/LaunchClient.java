package client;

import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class LaunchClient extends Application{

    private static LaunchClient instance;
    private GuiClient window;
    private Client client;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GuiClient gui = new GuiClient();
        primaryStage.setTitle("Client");

        Scene scene = new Scene(gui.getView(),1025,750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
