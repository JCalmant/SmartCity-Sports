package com.henallux.sportapp.DataAccess;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.sportapp.DataAccess.Utils.BaseDAO;
import com.henallux.sportapp.Model.Sport;
import com.henallux.sportapp.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 05-12-17.
 */

public class SportDAO extends BaseDAO{
    public SportDAO(String token) throws IOException {
        super(token,"https://sportappsmartcity.azurewebsites.net/api/Sports/");
    }

    public ArrayList<Sport> getAllSport() throws IOException, JSONException {
        JSONArray sports = super.getAll();
        Gson sport = new GsonBuilder().create();
        return sport.fromJson(sports.toString(),new TypeToken<ArrayList<Sport>>(){}.getType());
    }
}
