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
        Parent root = FXMLLoader.load(getClass().getResource("FXMLfilmes.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        MovieLog.initialize();
        Application.launch(args);
    }
}
