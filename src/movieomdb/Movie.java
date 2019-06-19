/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movieomdb;

import com.google.gson.JsonElement;
import resources.MovieLog;

/**
 *
 * @author ricarte
 */
public class Movie {

    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private JsonElement Ratings;
    private String Metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private boolean Response;

    public String getActors() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getActors");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getActors", Actors);
        return Actors;
    }

    public String getDirector() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getDirector");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getDirector", Director);
        return Director;
    }

    public String getImdbId() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getImdbId");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getImdbId", imdbID);
        return imdbID;
    }

    public String getTitle() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getTitle");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getTitle", Title);
        return Title;
    }

    public boolean validResponse() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "validResponse");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "validResponse", Response);
        return Response;
    }

    public String getYear() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getYear");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getYear", Year);
        return Year;
    }

    public String getRuntime() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getRuntime");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getRuntime", Runtime);
        return Runtime;
    }

    public String getImdbRating() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getImdbRating");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getImdbRating", imdbRating);
        return imdbRating;
    }

}
