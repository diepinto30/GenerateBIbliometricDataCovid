package modelos;

import java.util.List;
import static org.apache.jena.vocabulary.RDF.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Porti
 */
public class Source {
    
    
    String rank;
    String source_id;
    String issn;
    String type;
    String title;
    List<String> categorias;
    List<String> quartile;
    String country;
    String bestQ;

    public Source() {
    }

    public Source(String rank, String source_id, String issn, String type, String title, List<String> categorias, List<String> quartile, String country, String bestQ) {
        this.rank = rank;
        this.source_id = source_id;
        this.issn = issn;
        this.type = type;
        this.title = title;
        this.categorias = categorias;
        this.quartile = quartile;
        this.country = country;
        this.bestQ = bestQ;
    }
    
    
    

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<String> getQuartile() {
        return quartile;
    }

    public void setQuartile(List<String> quartile) {
        this.quartile = quartile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBestQ() {
        return bestQ;
    }

    public void setBestQ(String bestQ) {
        this.bestQ = bestQ;
    }
    
    

    

    
    
    
    
}
