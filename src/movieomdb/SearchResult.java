/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movieomdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import resources.MovieLog;

/**
 *
 * @author ricarte
 */
public class SearchResult {
    private final List<MovieCard> Search;
    private int totalResults;
    private boolean Response;
    
    public SearchResult() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "Constructor");
        Search = new ArrayList<>();
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "Constructor");
    }
    
    public int getTotalResults() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getTotalResults");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getTotalResults", totalResults);
        return totalResults;
    }
    
    public boolean validResponse() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "validResponse");
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "validResponse", Response);
        return Response;
    }
    
    public List<MovieCard> getResults() {
        MovieLog.LOG.entering(this.getClass().getSimpleName(), "getTotalResults");
        List<MovieCard> results = Collections.unmodifiableList(Search);
        MovieLog.LOG.exiting(this.getClass().getSimpleName(), "getTotalResults", results);
        return results;
    }
}
