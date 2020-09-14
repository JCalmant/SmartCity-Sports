package com.henallux.sportapp.DataAccess;

import android.util.Log;

import com.henallux.sportapp.DataAccess.Utils.JSONObjectMaker;
import com.henallux.sportapp.DataAccess.Utils.RequestMaker;
import com.henallux.sportapp.Exception.EmptyFieldException;
import com.henallux.sportapp.Exception.FormatException;
import com.henallux.sportapp.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by fran on 03-12-17.
 */

public class Inscription {
    private User user;
    public Inscription(String username,String password,String email) throws IOException, JSONException, FormatException, EmptyFieldException {
        user = new User(username,password,email);
    }

    public int sendInscription() throws IOException, JSONException {
        RequestMaker postInscription = new RequestMaker("https://sportappsmartcity.azurewebsites.net/api/Account");
        JSONObject dataToSend = JSONObjectMaker.toJSON("Username",user.getUserName(),"Password",user.getPassword(),"Email",user.getEmail());
        return postInscription.post(null,dataToSend);
    }
}
