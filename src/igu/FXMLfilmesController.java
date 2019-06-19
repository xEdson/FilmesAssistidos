/*
 * Faculdade de Tecnologia, UNICAMP
 * Professor responsável: Ivan L. M. Ricarte
 */
package igu;

import com.google.gson.Gson;
import filmesassistidos.MyMovieDb;
import filmesassistidos.WatchedMovie;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import movieomdb.MovieCard;
import movieomdb.SearchResult;
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
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "searchMovie", e);
        if (searchMovieTitle.isEmpty()) {
            // error: title is required
            Alert noTitleWindow = new Alert(AlertType.ERROR);
            noTitleWindow.setTitle("Error message");
            noTitleWindow.setHeaderText("No movie title");
            noTitleWindow.setContentText("Movie title must be provided for searching");
            noTitleWindow.showAndWait();
            searchMovieTitleTextField.clear();
            searchProductionYearTextField.clear();
        } else {
            String searchMovieYear = searchProductionYearTextField.getText();
            if (!searchMovieYear.chars().allMatch(Character::isDigit)) {
                // error: year must be numeric
                Alert noNumericYearWindow = new Alert(AlertType.ERROR);
                noNumericYearWindow.setTitle("Error message");
                noNumericYearWindow.setHeaderText("Invalid production year");
                noNumericYearWindow.setContentText("Production year must be numeric");
                noNumericYearWindow.showAndWait();
                searchMovieTitleTextField.clear();
                searchProductionYearTextField.clear();
            } else {
                // ok, search by title
                Gson imdbGson = new Gson();
                try {
                    ResourceBundle rb = ResourceBundle.getBundle("resources.config");
                    String key = rb.getString("key");
                    String urlBase = "http://www.omdbapi.com/?apikey=" + key + "&";
                    String url = urlBase + "s=" + searchMovieTitle.replace(' ', '+');
                    if (!searchMovieYear.isEmpty()) {
                        url = url.concat("&y=" + searchMovieYear);
                    }

                    URL imdbUrl = new URL(url);
                    InputStreamReader imdbSource = new InputStreamReader(imdbUrl.openStream());
                    String logDetail = "From URL " + url + " to class SearchResult";
                    MovieLog.LOG.entering("Gson", "fromJson", logDetail);
                    SearchResult search = imdbGson.fromJson(imdbSource, SearchResult.class);
                    MovieLog.LOG.exiting("Gson", "fromJson", search);

                    boolean responseStatus = search.validResponse();
                    if (responseStatus) {
                        // found some results, show them on table
                        TableView<SearchResultsModel> resultTable = new TableView();
                        TableColumn titleCol = new TableColumn("Title");
                        TableColumn yearCol = new TableColumn("Year");
                        TableColumn idCol = new TableColumn("IMDB id");
                        resultTable.getColumns().addAll(titleCol, yearCol, idCol);
                        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                        ObservableList<SearchResultsModel> tableData = FXCollections.observableArrayList();
                        for (MovieCard m : search.getResults()) {
                            tableData.add(new SearchResultsModel(m.getTitle(), m.getYear(), m.getImdbId()));
                        }
                        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
                        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
                        idCol.setCellValueFactory(new PropertyValueFactory<>("imdbId"));
                        resultTable.setItems(tableData);

                        Stage resultsView = new Stage();
                        resultsView.setScene(new Scene(resultTable));
                        resultsView.setTitle("Results for " + searchMovieTitle);
                        resultsView.initModality(Modality.WINDOW_MODAL);
                        resultsView.show();

                        // show details for selected movie from table
                        resultTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent ev) {
                                if (ev.getClickCount() > 1) {
                                    if (resultTable.getSelectionModel().getSelectedItem() != null) {
                                        SearchResultsModel selected = resultTable.getSelectionModel().getSelectedItem();
                                        String movieUrl = urlBase + "i=" + selected.getImdbId();
                                        try {
                                            URL selMovieUrl = new URL(movieUrl);
                                            InputStreamReader imdbSelSource = new InputStreamReader(selMovieUrl.openStream());
                                            String logDetail = "From URL " + selMovieUrl + " to class Movie";
                                            MovieLog.LOG.entering("Gson", "fromJson", logDetail);
                                            movieomdb.Movie selectedData = imdbGson.fromJson(imdbSelSource, movieomdb.Movie.class);
                                            MovieLog.LOG.exiting("Gson", "fromJson", selectedData);
                                            ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                                            ButtonType backButton = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
                                            String detailsContent = "Director: " + selectedData.getDirector()
                                                    + "\nActors: " + selectedData.getActors();
                                            Alert selectedWindow = new Alert(AlertType.CONFIRMATION,
                                                    detailsContent, confirmButton, backButton);
                                            selectedWindow.setTitle("Movie details");
                                            String movieHeader = selectedData.getTitle() + " [" + selectedData.getYear() + "]";
                                            selectedWindow.setHeaderText(movieHeader);
                                            Optional<ButtonType> result = selectedWindow.showAndWait();
                                            Stage storeStage = new Stage();
                                            if (result.isPresent() && result.get() == confirmButton) {
                                                // get personal information
                                                Label watchedDateLabel = new Label("Watched on");
                                                DatePicker watchedDatePicker = new DatePicker(LocalDate.now());
                                                Label personalRatingLabel = new Label("Personal rating");
                                                Slider personalRatingSlider = new Slider(0, 10, 0);
                                                personalRatingSlider.setShowTickMarks(true);
                                                personalRatingSlider.setShowTickLabels(true);
                                                personalRatingSlider.setMajorTickUnit(1);
                                                personalRatingSlider.setBlockIncrement(0.1f);
                                                personalRatingSlider.setSnapToTicks(true);
                                                Button storeButton = new Button("Store");
                                                Button storeCancelButton = new Button("Cancel");

                                                GridPane grid = new GridPane();
                                                grid.setPadding(new Insets(10, 10, 10, 10));
                                                grid.setVgap(10);
                                                grid.setHgap(10);
                                                grid.add(watchedDateLabel, 0, 0);
                                                grid.add(watchedDatePicker, 1, 0);
                                                grid.add(personalRatingLabel, 0, 1);
                                                grid.add(personalRatingSlider, 1, 1);
                                                grid.add(storeButton, 0, 2);
                                                grid.add(storeCancelButton, 1, 2);

                                                Scene scene = new Scene(grid);
                                                storeStage.setScene(scene);
                                                storeStage.setTitle("Personal info on " + movieHeader);
                                                storeStage.show();

                                                storeCancelButton.setOnAction(new EventHandler<ActionEvent>() {
                                                    @Override
                                                    public void handle(ActionEvent event) {
                                                        storeStage.close();
                                                    }
                                                });

                                                storeButton.setOnAction(new EventHandler<ActionEvent>() {
                                                    @Override
                                                    public void handle(ActionEvent event) {
                                                        // store on personal list of watched movies
                                                        LocalDate watchedDate = watchedDatePicker.getValue();
                                                        double rating = personalRatingSlider.getValue();
                                                        WatchedMovie myMovie = new WatchedMovie(selectedData.getImdbId(), selectedData.getTitle(), selectedData.getYear());
                                                        myMovie.setDirector(selectedData.getDirector());
                                                        String actors = selectedData.getActors();
                                                        StringTokenizer st = new StringTokenizer(actors, ";");
                                                        while (st.hasMoreTokens()) {
                                                            myMovie.addActor(st.nextToken());
                                                        }
                                                        myMovie.setImdbRating(selectedData.getImdbRating());
                                                        myMovie.setRuntime(selectedData.getRuntime());
                                                        myMovie.setMyRating(personalRatingSlider.getValue());
                                                        myMovie.addWatchedOn(watchedDatePicker.getValue());
                                                        movies.addMovie(myMovie);
                                                        storeStage.close();
                                                    }

                                                });

                                                resultsView.close();
                                                searchMovieTitleTextField.clear();
                                                searchProductionYearTextField.clear();
                                            }

                                        } catch (IOException ex) {
                                            Logger.getLogger(FXMLfilmesController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            }
                        });

                    }
                } catch (IOException ex) {
                    Logger.getLogger(FXMLfilmesController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    @FXML
    private void cleanSearchFields(ActionEvent e) {
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
        TableColumn titleCol = new TableColumn("Title");
        TableColumn yearCol = new TableColumn("Year");
        TableColumn directorCol = new TableColumn("Director");
        TableColumn timesWatchedCol = new TableColumn("Times watched");
        TableColumn watchedCol = new TableColumn("Last watched");
        TableColumn myRatingCol = new TableColumn("My rating");
        TableColumn imdbRatingCol = new TableColumn("IMDB rating");
        resultTable.getColumns().addAll(titleCol, yearCol, directorCol, timesWatchedCol, watchedCol, myRatingCol, imdbRatingCol);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Collection<WatchedMovie> myMovies = movies.getAllMovies();

        // filter results by title
        if (!showTitleTextField.getText().isEmpty()) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                if (m.getTitle().toLowerCase().contains(showTitleTextField.getText().toLowerCase())) {
                    filtered.add(m);
                }
            }
            myMovies = filtered;
        }

        // filter results by director's name
        if (!showDirectorTextField.getText().isEmpty()) {
            Collection<WatchedMovie> filtered = new ArrayList<>();
            for (WatchedMovie m : myMovies) {
                if (m.getDirector().toLowerCase().contains(showDirectorTextField.getText().toLowerCase())) {
                    filtered.add(m);
                }
            }
            myMovies = filtered;
        }

        // filter results by actors
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

        // filter results by date
        LocalDate fromDate = showFromDatePicker.getValue();
        LocalDate toDate = showToDatePicker.getValue();
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

        // filter results by rating
        double minRating = showEvaluationRangeSlider.getLowValue();
        double maxRating = showEvaluationRangeSlider.getHighValue();
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

    public static class SearchResultsModel {

        private final SimpleStringProperty title;
        private final SimpleStringProperty year;
        private final SimpleStringProperty imdbId;

        private SearchResultsModel(String title, String year, String id) {
            this.title = new SimpleStringProperty(title);
            this.year = new SimpleStringProperty(year);
            this.imdbId = new SimpleStringProperty(id);
        }

        public String getTitle() {
            return title.get();
        }

        public String getYear() {
            return year.get();
        }

        public String getImdbId() {
            return imdbId.get();
        }
    }

    public static class ShowResultsModel {

        private final SimpleStringProperty title;
        private final SimpleStringProperty year;
        private final SimpleStringProperty director;
        private final SimpleStringProperty watched;
        private final SimpleStringProperty imdbRating;
        private final SimpleStringProperty myRating;
        private final SimpleIntegerProperty count;

        public ShowResultsModel(WatchedMovie movie) {
            title = new SimpleStringProperty(movie.getTitle());
            year = new SimpleStringProperty(movie.getYear());
            director = new SimpleStringProperty(movie.getDirector());
            List<LocalDate> watchDates = movie.getWatchedOn();
            watched = new SimpleStringProperty(watchDates.get(watchDates.size() - 1).toString());
            imdbRating = new SimpleStringProperty(movie.getImdbRating());
            myRating = new SimpleStringProperty(String.format("%.1f", movie.getMyRating()));
            count = new SimpleIntegerProperty(movie.getWatchedCount());
        }

        public String getTitle() {
            return title.get();
        }

        public String getYear() {
            return year.get();
        }

        public String getDirector() {
            return director.get();
        }

        public String getWatched() {
            return watched.get();
        }

        public String getImdbRating() {
            return imdbRating.get();
        }

        public String getMyRating() {
            return myRating.get();
        }

        public int getCount() {
            return count.get();
        }

    }
}
