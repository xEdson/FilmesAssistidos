/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu;

import com.google.gson.Gson;
import filmesassistidos.MyMovieDb;
import filmesassistidos.SearchResultsModel;
import filmesassistidos.WatchedMovie;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import movieomdb.MovieCard;
import movieomdb.SearchResult;
import resources.MovieLog;

/**
 *
 * @author edsonpn
 */
public class RequestController {

    // ok, search by title
    private Gson imdbGson = new Gson();
    
    private String urlBase;
    
    private MyMovieDb movies;
    
    private FXMLfilmesController fxmlfilmesController = new FXMLfilmesController();
    
    SearchResult request(String searchMovieTitle, String searchMovieYear,ActionEvent e, MyMovieDb movie) {
        this.movies=movie;
        SearchResult search = new SearchResult();
        try {
            ResourceBundle rb = ResourceBundle.getBundle("resources.config");
            String key = rb.getString("key");
            urlBase = "http://www.omdbapi.com/?apikey=" + key + "&";
            String url = urlBase + "s=" + searchMovieTitle.replace(' ', '+');
            if (!searchMovieYear.isEmpty()) {
                url = url.concat("&y=" + searchMovieYear);
            }

            URL imdbUrl = new URL(url);
            InputStreamReader imdbSource = new InputStreamReader(imdbUrl.openStream());
            MovieLog.LOG.entering("Gson", "fromJson", url);
            search = imdbGson.fromJson(imdbSource, SearchResult.class);
            MovieLog.LOG.exiting("Gson", "fromJson", search);
           
            } catch (IOException ex) {
            Logger.getLogger(FXMLfilmesController.class.getName()).log(Level.SEVERE, null, ex);
        }
         if (search.validResponse()) {
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
                            String movieUrl = getUrlBase() + "i=" + selected.getImdbId();
                            try {
                                URL selMovieUrl = new URL(movieUrl);
                                InputStreamReader imdbSelSource = new InputStreamReader(selMovieUrl.openStream());
                                MovieLog.LOG.entering("Gson", "fromJson", selMovieUrl);
                                movieomdb.Movie selectedData = getImdbGson().fromJson(imdbSelSource, movieomdb.Movie.class);
                                MovieLog.LOG.exiting("Gson", "fromJson", selectedData);
                                ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                                ButtonType backButton = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
                                String detailsContent = "Director: " + selectedData.getDirector()
                                        + "\nActors: " + selectedData.getActors();
                                Alert selectedWindow = new Alert(Alert.AlertType.CONFIRMATION,
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
                                    fxmlfilmesController.cleanSearchFields(e);
                                }

                            } catch (IOException ex) {
                                Logger.getLogger(FXMLfilmesController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });

        }
        
        return search;
    }
    
    public String getUrlBase(){
        return this.urlBase;
    }
    
    public Gson getImdbGson(){
        return this.imdbGson;
    }
    

}
