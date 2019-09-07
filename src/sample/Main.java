package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private Stage primaryStage;
    private static Main main;

    public static Main getMain() {
        return main;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        main = this;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("resources/FXML/sample.fxml"));
        this.primaryStage = primaryStage;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
        loader.setResources(resourceBundle);
        AnchorPane mainAPane = loader.load();
        Scene scene = new Scene(mainAPane);
        //Locale.setDefault(new Locale("en"));
        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle(resourceBundle.getString("titleApp"));
        this.primaryStage.show();
    }

    public void setTitle(String title) {
        primaryStage.setTitle(title);
    }


    public static void main(String[] args) {

        launch(args);
    }
}
