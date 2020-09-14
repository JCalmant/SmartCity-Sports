package com.henallux.sportapp.DataAccess;

import com.henallux.sportapp.DataAccess.Utils.BaseDAO;
import com.henallux.sportapp.Model.Amitié;
import com.henallux.sportapp.Model.DTO.AmiDTO;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by fran on 06-12-17.
 */

public class AmitiésDAO extends BaseDAO{

    public AmitiésDAO(String token) throws IOException {
        super(token, "https://sportappsmartcity.azurewebsites.net/api/Amitie/");
    }

    public void post(Amitié amitié) throws JSONException, IOException {
        super.post(amitié);
    }

    public void delete(AmiDTO amitié) throws JSONException, IOException {
        super.delete(amitié);
    }

    public void put(AmiDTO amiDTO) throws IOException, JSONException {
        super.put(amiDTO,null);
    }
}
