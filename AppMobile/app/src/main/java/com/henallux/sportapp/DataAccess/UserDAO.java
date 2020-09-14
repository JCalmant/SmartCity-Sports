package com.henallux.sportapp.DataAccess;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.sportapp.DataAccess.Utils.BaseDAO;
import com.henallux.sportapp.Model.DTO.DisponibiliteDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.Sport;
import com.henallux.sportapp.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fran on 06-12-17.
 */

public class UserDAO extends BaseDAO{
    public static final int COMPLEXE = 0;
    public static final int SPORT = 1;

    public UserDAO(String token) throws IOException {
        super(token,"https://sportappsmartcity.azurewebsites.net/api/Utilisateurs/");
    }

    public void put(User user) throws IOException, JSONException {
        super.put(user, user.getUserName());
    }

    public UserDTO getByUsername(String username) throws IOException, JSONException {
        JSONObject jsonUser = super.getBy(username);
        Gson user = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return user.fromJson(jsonUser.toString(),UserDTO.class);
    }

    public ArrayList<String> getSportsUser(String username) throws IOException, JSONException {
        UserDTO user = getByUsername(username);
        return getAllComplexeOrSport(user,UserDAO.SPORT);
    }

    public ArrayList<String> getAllComplexeUser(String username) throws IOException, JSONException {
        UserDTO user = getByUsername(username);
        return getAllComplexeOrSport(user,UserDAO.COMPLEXE);
    }

    public static ArrayList<String> getAllComplexeOrSport(UserDTO userDTO, int sportOrComplexe){
        ArrayList<String> list = new ArrayList<>();
        switch (sportOrComplexe){
            case UserDAO.COMPLEXE:
                for(DisponibiliteDTO dispo : userDTO.getDisponibilites()){
                    if(dispo.getComplexeSportif()!=null && !list.contains(dispo.getComplexeSportif()))
                        list.add(dispo.getComplexeSportif());
                }
                break;
            case UserDAO.SPORT:
                for(DisponibiliteDTO dispo : userDTO.getDisponibilites()) {
                    if (!list.contains(dispo.getLibelléSport()))list.add(dispo.getLibelléSport());
                }
                break;
        }
        return list;
    }

    public ArrayList<UserDTO> getUserBySport(ArrayList<String> sports, String username) throws IOException, JSONException {
        ArrayList<UserDTO> userDTOS=getAllUser();
        ArrayList<UserDTO> usersFound= new ArrayList<>();
        for(UserDTO user : userDTOS){
            int i = 0;
            while(i<user.getDisponibilites().size() && !usersFound.contains(user)){
                int j = 0;
                while(j<sports.size() && !user.getDisponibilites().get(i).getLibelléSport().equals(sports.get(j))){
                    j++;
                }
                if(j<sports.size() && !user.getUsername().equals(username)){
                    usersFound.add(user);
                }
                i++;
            }
        }
        return usersFound;
    }

    public ArrayList<UserDTO> getAllUser() throws IOException, JSONException {
        JSONArray users = super.getAll();
        Gson gsonUsers = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gsonUsers.fromJson(users.toString(),new TypeToken<ArrayList<UserDTO>>(){}.getType());
    }
}