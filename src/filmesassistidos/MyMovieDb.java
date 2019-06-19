/*
 * Faculdade de Tecnologia, UNICAMP
 * Professor responsável: Ivan L. M. Ricarte
 */
package filmesassistidos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import resources.MovieLog;

/**
 * @author ricarte at ft.unicamp.br
 */
public class MyMovieDb {
    
    private Map<String, WatchedMovie> myMovies;
    private final String movieStore = "meusfilmes.dat";
    
    public MyMovieDb() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "Constructor");
        Path p = Paths.get(movieStore);
        if (Files.exists(p)) {
            load();            
        } else {
            MovieLog.LOG.fine("Creating new collection");
            myMovies = new HashMap<>();
        }
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "Constructor");
    }
    
    public void addMovie(WatchedMovie movie) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "addMovie", movie);
        WatchedMovie stored = myMovies.get(movie.getImdbId());
        if (stored == null) {
            myMovies.put(movie.getImdbId(), movie);
        } else {
            List<LocalDate> watchedList = movie.getWatchedOn();
            LocalDate watched = watchedList.get(watchedList.size() - 1);
            stored.addWatchedOn(watched);
            stored.setMyRating(movie.getMyRating());
        }
        save();
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "addMovie");
    }
    
    public WatchedMovie getMovie(String id) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getMovie", id);
        WatchedMovie movie = myMovies.get(id);
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getMovie", movie);
        return movie;
    }
    
    public Collection<WatchedMovie> getAllMovies() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getAllMovies");
        Collection<WatchedMovie> movies = myMovies.values();
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getMovie", movies);
        return movies;
    }
    
    private void save() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "save");
        Path p = Paths.get(movieStore);
        
        try {
            OutputStream os = Files.newOutputStream(p);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(myMovies);
        } catch (IOException ex) {
            MovieLog.LOG.log(Level.SEVERE, null, ex);
        }
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "save");
    }
    
    private void load() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "load");
        Path p = Paths.get(movieStore);
        
        try {
            InputStream is = Files.newInputStream(p);
            ObjectInputStream ois = new ObjectInputStream(is);
            myMovies = (HashMap) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            MovieLog.LOG.log(Level.SEVERE, null, ex);
        }
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "load");
    }
}
