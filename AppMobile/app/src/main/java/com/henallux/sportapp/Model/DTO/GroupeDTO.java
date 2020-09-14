package com.henallux.sportapp.Model.DTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fran on 20-12-17.
 */

public class GroupeDTO implements Serializable{
    private int groupeId;
    private ArrayList<String> membre;

    public GroupeDTO(){}

    public GroupeDTO(int groupeId, ArrayList<String> membre){
        this.groupeId = groupeId;
        this.membre = membre;
    }

    public ArrayList<String> getMembre() {
        if(this.membre==null)
            this.membre=new ArrayList<>();
        return membre;
    }

    public int getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(int groupeId) {
        this.groupeId = groupeId;
    }

    public void setMembre(ArrayList<String> membre) {
        this.membre = membre;
    }
}
