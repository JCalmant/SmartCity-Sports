package com.henallux.sportapp.DataAccess.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fran on 04-12-17.
 */

public class JSONObjectMaker {

    public static JSONObject toJSON(Object ... o) throws JSONException {
        JSONObject json = new JSONObject();
        if(o.length%2==0){
            for(int i=0; i<o.length;i+=2){
                json.accumulate(o[i].toString(),o[i+1]);
            }
        }
        return json;
    }
}
