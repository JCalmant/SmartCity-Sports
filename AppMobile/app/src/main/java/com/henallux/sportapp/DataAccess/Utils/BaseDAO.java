package com.henallux.sportapp.DataAccess.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by fran on 05-12-17.
 */

public class BaseDAO {
    private String token;
    private RequestMaker request;
    protected BaseDAO(String token, String url) throws IOException {
        this.token=token;
        request = new RequestMaker(url);
    }

    protected JSONObject getBy(String additional) throws IOException, JSONException {
        if(additional!=null)request.addAddtionalFieldToURL(additional);
        return request.getBy(token);
    }

    protected JSONArray getAllBy(String additional) throws IOException, JSONException {
        request.addAddtionalFieldToURL(additional);
        return request.getAll(token);
    }

    protected JSONArray getAll() throws IOException, JSONException {
        return request.getAll(token);
    }

    protected void put(Object dataToPut, String additional) throws IOException, JSONException {
        if(additional!=null)request.addAddtionalFieldToURL(additional);
        request.put(token, commonPutPost(dataToPut));
    }

    protected void post(Object dataToPost) throws IOException, JSONException {
        request.post(token, commonPutPost(dataToPost));
    }

    protected int postWithBodyResponse(Object dataToPost) throws JSONException, IOException {
        return request.postWithBodyResponse(token,commonPutPost(dataToPost));
    }

    protected void delete(Object dataToDelete) throws JSONException, IOException {
        request.delete(token,commonPutPost(dataToDelete));
    }

    private JSONObject commonPutPost(Object data) throws JSONException {
        Gson gson = new GsonBuilder().create();
        String formatJson = gson.toJson(data);
        return new JSONObject(formatJson);
    }
}