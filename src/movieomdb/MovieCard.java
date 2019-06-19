/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movieomdb;

import resources.MovieLog;

/**
 *
 * @author ricarte
 */
public class MovieCard {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

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

    public String getYear() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getYear");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getYear", Year);
        return Year;
    }

}
