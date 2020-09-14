package com.henallux.sportapp.DataAccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.sportapp.DataAccess.Utils.BaseDAO;
import com.henallux.sportapp.Model.DTO.DisponibiliteDTO;
import com.henallux.sportapp.Model.Disponibilite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 06-12-17.
 */

public class DisponibilitéDAO extends BaseDAO {
    public DisponibilitéDAO(String token) throws IOException {
        super(token,"https://sportappsmartcity.azurewebsites.net/api/Disponibilites/");
    }

    public void post(DisponibiliteDTO disponibilite) throws JSONException, IOException {
        super.post(disponibilite);
    }

    public ArrayList<DisponibiliteDTO> getAllDisponibilé() throws IOException, JSONException {
        JSONArray dispo = super.getAll();
        Gson gsonDispo = new GsonBuilder().create();
        return gsonDispo.fromJson(dispo.toString(),new TypeToken<ArrayList<DisponibiliteDTO>>(){}.getType());
    }

    public void delete(DisponibiliteDTO disponibiliteDTO) throws IOException, JSONException {
        super.delete(disponibiliteDTO);
    }
}