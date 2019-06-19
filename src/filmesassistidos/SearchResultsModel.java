/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmesassistidos;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author edsonpn
 */
public class SearchResultsModel {

    private final SimpleStringProperty title;
    private final SimpleStringProperty year;
    private final SimpleStringProperty imdbId;

    public SearchResultsModel(String title, String year, String id) {
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
