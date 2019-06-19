/*
 * Faculdade de Tecnologia, UNICAMP
 * Professor responsável: Ivan L. M. Ricarte
 */
package igu;

import filmesassistidos.MyMovieDb;
import filmesassistidos.ShowResultsModel;
import filmesassistidos.WatchedMovie;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import resources.MovieLog;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class FXMLfilmesController implements Initializable {

    @FXML
    private Tab searchMovieTab;
    @FXML
    private TextField searchMovieTitleTextField;
    @FXML
    private TextField searchProductionYearTextField;
    @FXML
    private Button searchNewMovieButton;
    @FXML
    private Button searchClearButton;
    @FXML
    private Tab showMoviesTab;
    @FXML
    private TextField showTitleTextField;
    @FXML
    private TextField showDirectorTextField;
    @FXML
    private TextField showStarsTextField;
    @FXML
    private Button showMoviesButton;
    @FXML
    private Button showClearButton;
    @FXML
    private DatePicker showFromDatePicker;
    @FXML
    private DatePicker showToDatePicker;
    @FXML
    private Pane showEvaluationPane;

    private RangeSlider showEvaluationRangeSlider;
    private MyMovieDb movies;

    private final TableColumn titleCol = new TableColumn("Title");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movies = new MyMovieDb();
        showEvaluationRangeSlider = new RangeSlider(0, 10, 0, 10);
        showEvaluationRangeSlider.setShowTickMarks(true);
        showEvaluationRangeSlider.setShowTickLabels(true);
        showEvaluationRangeSlider.setMajorTickUnit(1);
        showEvaluationRangeSlider.setMinorTickCount(4);
        showEvaluationRangeSlider.setSnapToTicks(true);
        showEvaluationPane.getChildren().add(showEvaluationRangeSlider);
    }

    @FXML
    private void searchMovie(ActionEvent e) {
        String searchMovieTitle = searchMovieTitleTextField.getText();
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "searchMovie", searchMovieTitle);
        if (validateSearchResultTitle(searchMovieTitle)) {
            String searchMovieYear = searchProductionYearTextField.getText();
            if (validadeSearchResultYear(searchMovieYear)) {
                RequestController requestController = new RequestController();
                requestController.request(searchMovieTitle, searchMovieYear, e, movies);

            }
        }
    }

    private boolean validadeSearchResultYear(String searchMovieYear) {
        if (!searchMovieYear.chars().allMatch(Character::isDigit)) {
            // error: year must be numeric
            Alert noNumericYearWindow = new Alert(AlertType.ERROR);
            noNumericYearWindow.setTitle("Error message");
            noNumericYearWindow.setHeaderText("Invalid production year");
            noNumericYearWindow.setContentText("Production year must be numeric");
            noNumericYearWindow.showAndWait();
            searchMovieTitleTextField.clear();
            searchProductionYearTextField.clear();
            return false;
        }
        return true;
    }

    private boolean validateSearchResultTitle(String searchMovieTitle) {
        if (searchMovieTitle.isEmpty()) {
            // error: title is required
            Alert noTitleWindow = new Alert(AlertType.ERROR);
            noTitleWindow.setTitle("Error message");
            noTitleWindow.setHeaderText("No movie title");
            noTitleWindow.setContentText("Movie title must be provided for searching");
            noTitleWindow.showAndWait();
            searchMovieTitleTextField.clear();
            searchProductionYearTextField.clear();
            return false;
        }
        return true;
    }

    @FXML
    void cleanSearchFields(ActionEvent e) {
        searchMovieTitleTextField.clear();
        searchProductionYearTextField.clear();
    }

    @FXML
    private void cleanShowFields(ActionEvent e) {
        showTitleTextField.clear();
        showDirectorTextField.clear();
        showStarsTextField.clear();
        showFromDatePicker.setValue(null);
        showToDatePicker.setValue(null);
        showEvaluationRangeSlider.setLowValue(0);
        showEvaluationRangeSlider.setHighValue(10);
    }

    @FXML
    private void showMovies(ActionEvent e) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "showMovies");
        TableView<ShowResultsModel> resultTable = new TableView();
        TableColumn yearCol = new TableColumn("Year");
        TableColumn directorCol = new TableColumn("Director");
        TableColumn timesWatchedCol = new TableColumn("Times watched");
        TableColumn watchedCol = new TableColumn("Last watched");
        TableColumn myRatingCol = new TableColumn("My rating");
        TableColumn imdbRatingCol = new TableColumn("IMDB rating");
        resultTable.getColumns().addAll(titleCol, yearCol, directorCol, timesWatchedCol, watchedCol, myRatingCol, imdbRatingCol);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Collection<WatchedMovie> myMovies = movies.getAllMovies();

        myMovies = filterMovies(myMovies);

        // filter results by rating
        double minRating = showEvaluationRangeSlider.getLowValue();
        double maxRating = showEvaluationRangeSlider.getHighValue();
        myMovies = filterByRating(myMovies, minRating, maxRating);

        ObservableList<ShowResultsModel> tableData = FXCollections.observableArrayList();
        for (WatchedMovie m : myMovies) {
            tableData.add(new ShowResultsModel(m));
        }
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        timesWatchedCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        watchedCol.setCellValueFactory(new PropertyValueFactory<>("watched"));
        myRatingCol.setCellValueFactory(new PropertyValueFactory<>("myRating"));
        imdbRatingCol.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        resultTable.setItems(tableData);

        Stage resultsView = new Stage();
        resultsView.setScene(new Scene(resultTable));
        resultsView.setTitle("Watched movies");
        resultsView.initModality(Modality.WINDOW_MODAL);

        resultsView.show();
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "showMovies");
    }

    private Collection<WatchedMovie> filterMovies(Collection<WatchedMovie> myMovies) {
        // filter results by title
        myMovies = filterByTitle(myMovies);
        // filter results by director's name
        myMovies = filterByDirector(myMovies);
        // filter results by actors
        myMovies = filterByActors(myMovies);
        // filter results by date
        LocalDate fromDate = showFromDatePicker.getValue();
        LocalDate toDate = showToDatePicker.getValue();
        myMovies = filterByDate(myMovies, fromDate, toDate);
        return myMovies;
    }

    private Collection<WatchedMovie> filterByRating(Collection<WatchedMovie> myMovies, double minRating, double maxRating) {
        if (minRating > 0 || maxRating < 10) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                double myRating = m.getMyRating();
                if (myRating >= minRating && myRating <= maxRating) {
                    filtered.add(m);
                }
            }
            myMovies = filtered;
        }
        return myMovies;
    }

    private Collection<WatchedMovie> filterByTitle(Collection<WatchedMovie> myMovies) {
        if (!showTitleTextField.getText().isEmpty()) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                if (m.getTitle().toLowerCase().contains(showTitleTextField.getText().toLowerCase())) {
                    filtered.add(m);
                }
            }
            myMovies = filtered;
        }
        return myMovies;
    }

    private Collection<WatchedMovie> filterByDate(Collection<WatchedMovie> myMovies, LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                List<LocalDate> dates = m.getWatchedOn();
                for (LocalDate d : dates) {
                    if (d.compareTo(fromDate) >= 0 && d.compareTo(toDate) <= 0) {
                        filtered.add(m);
                    }
                }
            }

            myMovies = filtered;
        }

        return myMovies;
    }

    private Collection<WatchedMovie> filterByActors(Collection<WatchedMovie> myMovies) {
        if (!showStarsTextField.getText().isEmpty()) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                List<String> actors = m.getActors();
                for (String actor : actors) {
                    if (actor.toLowerCase().contains(showStarsTextField.getText().toLowerCase())) {
                        filtered.add(m);
                    }
                }
            }
            myMovies = filtered;
        }
        return myMovies;
    }

    private Collection<WatchedMovie> filterByDirector(Collection<WatchedMovie> myMovies) {
        if (!showDirectorTextField.getText().isEmpty()) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                if (m.getDirector().toLowerCase().contains(showDirectorTextField.getText().toLowerCase())) {
                    filtered.add(m);
                }
            }
            myMovies = filtered;
        }

        return myMovies;
    }
}
