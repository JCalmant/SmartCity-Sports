package com.henallux.sportapp.DataAccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.sportapp.DataAccess.Utils.BaseDAO;
import com.henallux.sportapp.Model.DTO.GroupeDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 20-12-17.
 */

public class GroupeDAO extends BaseDAO{

    public GroupeDAO(String token) throws IOException {
        super(token, "https://sportappsmartcity.azurewebsites.net/api/AttributionGroupes/");
    }

    public ArrayList<GroupeDTO> getAllUserGroupes(String username) throws IOException, JSONException {
        JSONArray array = super.getAllBy(username);
        Gson gsonGroupes = new GsonBuilder().create();
        return gsonGroupes.fromJson(array.toString(),new TypeToken<ArrayList<GroupeDTO>>(){}.getType());
    }

    public int post(GroupeDTO groupeDTO) throws IOException, JSONException {
        return super.postWithBodyResponse(groupeDTO);
    }
}
