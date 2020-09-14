package com.henallux.sportapp.Model;

import com.henallux.sportapp.DataAccess.Utils.BaseDAO;

/**
 * Created by fran on 06-12-17.
 */

public class Amitié {
    private String ajoutéId, ajouteurId;
    private Boolean estAccepté;
    public static final boolean AMI = true;
    public static final boolean PASAMI = false;

    public Amitié(String ajouté,String ajouteur){
        this.ajouteurId = ajouteur;
        this.ajoutéId = ajouté;
    }

    public Amitié(String ajouté, String ajouteur,Boolean accepté) {
        this(ajouté,ajouteur);
        this.estAccepté=accepté;
    }

    public void setAccepté(Boolean accepté) {
        this.estAccepté = accepté;
    }

    public Boolean isAccepté() {
        return estAccepté;
    }
}