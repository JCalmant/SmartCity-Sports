package com.henallux.sportapp.DataAccess;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.sportapp.DataAccess.Utils.BaseDAO;
import com.henallux.sportapp.Model.ComplexeSportif;
import com.henallux.sportapp.Model.DTO.ComplexeSportifDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 15-12-17.
 */

public class ComplexeSportifDAO extends BaseDAO {

    public ComplexeSportifDAO(String token) throws IOException {
        super(token,"https://sportappsmartcity.azurewebsites.net/api/ComplexeSportifs/");
    }

    public ArrayList<ComplexeSportifDTO> getAllComplexes() throws IOException, JSONException {
        JSONArray dispo = super.getAll();
        Gson gsonDispo = new GsonBuilder().create();
        return gsonDispo.fromJson(dispo.toString(),new TypeToken<ArrayList<ComplexeSportifDTO>>(){}.getType());
    }

    public ArrayList<ComplexeSportifDTO> getAllComplexesForXSport(ArrayList<String> sports) throws IOException, JSONException {
        ArrayList<ComplexeSportifDTO> complexeSportifDTOS = this.getAllComplexes();
        ArrayList<ComplexeSportifDTO> complexeMatched = new ArrayList<>();
        for (ComplexeSportifDTO complexeSportif : complexeSportifDTOS){
            int i = 0;
            while(i<sports.size()){
                int j=0;
                while(j<complexeSportif.getDisponibilites().size()){
                    if(complexeSportif.getDisponibilites().get(j).getLibelléSport().equals(sports.get(i))){
                        complexeMatched.add(complexeSportif);
                        i=sports.size();
                        j=complexeSportif.getDisponibilites().size();
                    }
                    j++;
                }
                i++;
            }
        }
        return complexeMatched;
    }

    public ComplexeSportifDTO getComplexeByName(String name) throws IOException, JSONException {
        ArrayList<ComplexeSportifDTO> complexes=this.getAllComplexes();
        int i=0;
        while(!complexes.get(i).getLibellé().equals(name)){
            i++;
        }
        return complexes.get(i);
    }
}