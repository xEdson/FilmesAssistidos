/*
 * Faculdade de Tecnologia, UNICAMP
 * Professor responsável: Ivan L. M. Ricarte
 */

package igu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.MovieLog;

/**
 * Classe FilmesAssistidos
 * @author ricarte at ft.unicamp.br
 */
public class FilmesAssistidos extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MovieLog.LOG.entering("Application", "start");
        Parent root = FXMLLoader.load(getClass().getResource("FXMLfilmes.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        MovieLog.LOG.exiting("Application", "start");
    }

    public static void main(String[] args) {
        MovieLog.initialize();
        Application.launch(args);
    }
}
