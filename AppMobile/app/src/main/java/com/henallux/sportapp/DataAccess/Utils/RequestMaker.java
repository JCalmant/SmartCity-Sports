package com.henallux.sportapp.DataAccess.Utils;

import android.util.Log;

import com.henallux.sportapp.DataAccess.RecupToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fran on 03-12-17.
 */

public class RequestMaker {
    private URL url;
    private HttpURLConnection connection;

    public static final int GET = 0;
    public static final int PUT = 1;
    public static final int POST = 2;
    public static final int DELETE = 3;


    public RequestMaker(String url) throws IOException {
        this.url = new URL(url);
        connection = (HttpURLConnection)this.url.openConnection();
    }

    public int post(String token,JSONObject dataToSend) throws IOException {

        return commonRequest(token,"POST",dataToSend);
    }

    private String get(String token) throws IOException, JSONException {
        if(token!=null)
            connection.addRequestProperty("Authorization","Bearer "+token);

        connection.setRequestMethod("GET");

        InputStream dataReceived = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataReceived));
        StringBuilder builder = new StringBuilder();
        String stringJSON="",line;

        while((line=reader.readLine())!=null){
            builder.append(line);
        }

        reader.close();

        return builder.toString();
    }

    public JSONArray getAll(String token) throws IOException, JSONException {
        return new JSONArray(get(token));
    }

    public JSONObject getBy(String token) throws IOException, JSONException {
        return new JSONObject(get(token));
    }

    public int put(String token,JSONObject dataToPut) throws IOException, JSONException {


        return commonRequest(token,"PUT",dataToPut);
    }

    public int delete(String token, JSONObject dataToDelete) throws IOException {

        return commonRequest(token,"DELETE",dataToDelete);

    }

    public int postWithBodyResponse(String token,JSONObject dataToPost) throws IOException, JSONException {

        connection.addRequestProperty("Authorization","Bearer "+token);
        connection.addRequestProperty("Content-Type","application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream dataToPostMethod = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(dataToPostMethod);

        connection.connect();

        writer.write(dataToPost.toString());
        writer.flush();
        writer.close();

        InputStream in = new BufferedInputStream(connection.getInputStream());
        String chatRoom = RecupToken.convertStreamToString(in);

        dataToPostMethod.close();
        connection.disconnect();

        JSONObject jsonChatRoom= new JSONObject(chatRoom);
        int ChatRoomNumber = jsonChatRoom.getInt("NumeroChat");
        return ChatRoomNumber;
    }

    public void addAddtionalFieldToURL(String additionalField) throws IOException {
        URL urlTransition= new URL(url.toString()+additionalField);
        connection = (HttpURLConnection)urlTransition.openConnection();
    }

    private int commonRequest(String token, String method, JSONObject data) throws IOException {
        if(token!=null)
            connection.addRequestProperty("Authorization","Bearer "+token);

        connection.addRequestProperty("Content-Type","application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod(method);

        OutputStream dataToPostMethod = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(dataToPostMethod);

        connection.connect();

        writer.write(data.toString());
        writer.flush();
        writer.close();

        int reponse = connection.getResponseCode();

        dataToPostMethod.close();
        connection.disconnect();

        return reponse;
    }
}