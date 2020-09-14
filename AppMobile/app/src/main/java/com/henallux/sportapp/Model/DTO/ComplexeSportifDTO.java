package com.henallux.sportapp.Model.DTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fran on 15-12-17.
 */

public class ComplexeSportifDTO implements Serializable{
    private double coorY;
    private double coorX;
    private String siteWeb;
    private String adresse;
    private String libellé;
    private ArrayList<DisponibiliteDTO> disponibilites;

    public ComplexeSportifDTO(){

    }

    public ArrayList<DisponibiliteDTO> getDisponibilites() {
        return disponibilites;
    }

    public void setDisponibilites(ArrayList<DisponibiliteDTO> disponibilites) {
        this.disponibilites = disponibilites;
    }

    public double getCoorX() {
        return coorX;
    }

    public double getCoorY() {
        return coorY;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getLibellé() {
        return libellé;
    }

    public String getSiteWeb() {
        return siteWeb;
    }


    public ArrayList<String> getSports(){
        ArrayList<String> sports = new ArrayList<>();
        for(DisponibiliteDTO dispo : this.disponibilites){
            if(!sports.contains(dispo.getLibelléSport()))sports.add(dispo.getLibelléSport());
        }
        return sports;
    }
}
