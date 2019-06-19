/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmesassistidos;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author edsonpn
 */
public class ShowResultsModel {
    
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
