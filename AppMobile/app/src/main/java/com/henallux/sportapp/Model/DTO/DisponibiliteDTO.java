package com.henallux.sportapp.Model.DTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fran on 10-12-17.
 */

public class DisponibiliteDTO implements Serializable {
    private String username;
    private String complexeSportif;
    private String libelléSport;

    public DisponibiliteDTO(){

    }

    public String getComplexeSportif() {
        return complexeSportif;
    }

    public String getLibelléSport() {
        return libelléSport;
    }

    public String getUsername() {
        return username;
    }

    public void setComplexeSportif(String complexeSportif) {
        this.complexeSportif =  complexeSportif;
    }

    public void setLibelléSport(String libelléSport) {
        this.libelléSport = libelléSport;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}