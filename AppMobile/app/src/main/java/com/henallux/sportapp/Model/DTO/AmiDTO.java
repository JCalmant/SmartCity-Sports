package com.henallux.sportapp.Model.DTO;

import java.io.Serializable;

/**
 * Created by fran on 10-12-17.
 */

public class AmiDTO implements Serializable{
    private String amiAjouté;
    private String amiAjouteur;
    private boolean accepté;

    public AmiDTO(){

    }

    public void setAccepté(boolean accepté) {
        this.accepté = accepté;
    }

    public boolean isAccepté() {
        return accepté;
    }

    public String getAmiAjouteur() {
        return amiAjouteur;
    }

    public String getAmiAjouté() {
        return amiAjouté;
    }

    public void setAmiAjouteur(String amiAjouteur) {
        this.amiAjouteur = amiAjouteur;
    }

    public void setAmiAjouté(String amiAjouté) {
        this.amiAjouté = amiAjouté;
    }

    public String getAmi(String username){
        if(accepté)
            return username.equals(amiAjouteur)?amiAjouté:amiAjouteur;

        return null;
    }
}