package com.henallux.sportapp.DataAccess;

import android.support.annotation.Nullable;

import com.henallux.sportapp.DataAccess.Utils.JSONObjectMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fran on 02-12-17.
 */

public class RecupToken{
    private URL url;
    private HttpURLConnection connection;
    private String token;
    public RecupToken() throws IOException {
        url = new URL("https://sportappsmartcity.azurewebsites.net/api/jwt");
    }

    public String getToken(String userName,String password) throws IOException, JSONException {
        connection=(HttpURLConnection) url.openConnection();
        JSONObject dataToSend = JSONObjectMaker.toJSON("UserName",userName,"Password",password);

        connection.addRequestProperty("Content-Type","application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream dataToPostMethod = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(dataToPostMethod);

        connection.connect();

        writer.write(dataToSend.toString());
        writer.flush();
        writer.close();

        InputStream in = new BufferedInputStream(connection.getInputStream());
        token = convertStreamToString(in);

        dataToPostMethod.close();
        connection.disconnect();

        JSONObject tokenBrutWithExpireIn= new JSONObject(token);
        token = tokenBrutWithExpireIn.getString("access_token");
        return token;
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static RecupToken recupToken(){
        try {
            return new RecupToken();
        }
        catch (Exception e){
        }
        return null;
    }

}
