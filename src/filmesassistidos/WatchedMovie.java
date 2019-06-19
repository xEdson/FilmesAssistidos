/*
 * Faculdade de Tecnologia, UNICAMP
 * Professor responsável: Ivan L. M. Ricarte
 */
package filmesassistidos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import resources.MovieLog;

/**
 * @author ricarte at ft.unicamp.br
 */
public class WatchedMovie implements java.io.Serializable {

    private final String imdbId;
    private final String title;
    private final String year;
    private String director;
    private final List<String> actors;
    private String runtime;
    private String imdbRating;
    private double myRating;
    private List<LocalDate> watchedOn;

    public WatchedMovie(String id, String title, String year) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "Constructor");
        imdbId = id;
        this.title = title;
        this.year = year;
        actors = new ArrayList<>();
        watchedOn = new ArrayList<>();
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "Constructor");
    }

    public String getTitle() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getTitle");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getTitle", title);
        return title;
    }

    public String getYear() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getYear");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getYear", year);
        return year;
    }

    public String getDirector() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getDirector");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getDirector", director);
        return director;
    }

    public void setDirector(String name) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "setDirector", name);
        director = name;
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "setDirector");        
    }

    public void addActor(String name) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "addActor", name);
        actors.add(name);
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "addActor");
    }

    public List<String> getActors() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getActors");
        List<String> actorsList = Collections.unmodifiableList(actors);
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getActors", actorsList);
        return actorsList;
    }

    public String getRuntime() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getRuntime");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getRuntime", runtime);
        return runtime;
    }

    public void setRuntime(String runtime) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "setRuntime", runtime);
        this.runtime = runtime;
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "setRuntime");    
    }

    public String getImdbRating() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getImdbRating");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getImdbRating", imdbRating);
        return imdbRating;      
    }

    public void setImdbRating(String imdbRating) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "setImdbRating", imdbRating);
        this.imdbRating = imdbRating;
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "setImdbRating");
    }

    public double getMyRating() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getMyRating");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getMyRating", myRating);
        return myRating;
    }

    public void setMyRating(double myRating) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "setMyRating", imdbRating);
        this.myRating = myRating;
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "setMyRating");
    }

    public List<LocalDate> getWatchedOn() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getWatchedOn");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getWatchedOn", watchedOn);
        return watchedOn;
    }

    public int getWatchedCount() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getWatchedCount");
        int count = watchedOn.size();
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getWatchedCount", count);
        return count;
    }

    public void addWatchedOn(LocalDate watchedOn) {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "addWatchedOn", watchedOn);
        this.watchedOn.add(watchedOn);
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "addWatchedOn");
    }

    @Override
    public String toString() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "toString");
        StringBuffer actorsList = new StringBuffer();
        for (String actor : actors) {
            actorsList.append(actor + "; ");
        }
        StringBuffer watchedList = new StringBuffer();
        for (LocalDate d : watchedOn) {
            watchedList.append(d.toString() + "; ");
        }
        String description = "WatchedMovie{" + "imdbId=" + imdbId + ", title=" + title + ", year=" + year + ", director=" + director + ", actors=" + actorsList + ", runtime=" + runtime + ", imdbRating=" + imdbRating + ", myRating=" + myRating + ", watchedOn=" + watchedList + '}';
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "toString", description);
        return description;
    }

    public String getImdbId() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getImdbId");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getImdbId", imdbId);
        return imdbId;
    }

}
