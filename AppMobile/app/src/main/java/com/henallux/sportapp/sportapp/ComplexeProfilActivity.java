package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.henallux.sportapp.DataAccess.DisponibilitéDAO;
import com.henallux.sportapp.Model.ComplexeSportif;
import com.henallux.sportapp.Model.DTO.ComplexeSportifDTO;
import com.henallux.sportapp.Model.DTO.DisponibiliteDTO;
import com.henallux.sportapp.Model.Sport;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 15-12-17.
 */

public class ComplexeProfilActivity extends AppCompatActivity implements OnMapReadyCallback{
    private TextView name,address,webSite;
    private Button addDispo;
    private LinearLayout sportsPraticlables;
    private ComplexeSportifDTO complexe;
    private ArrayList <String> sportsSélectionnés;
    private ResearchActivity researchActivity;
    private SharedPreferences preferences;
    private Intent mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complexe_profil);
        name = (TextView) findViewById(R.id.ComplexeName);
        address = (TextView)findViewById(R.id.ComplexeAddress);
        webSite = (TextView)findViewById(R.id.ComplexeWebSite);
        addDispo = (Button)findViewById(R.id.ButtonAddDispo);
        sportsPraticlables = (LinearLayout)findViewById(R.id.ContainerSportPraticable);
        complexe = (ComplexeSportifDTO) getIntent().getSerializableExtra("Complexe");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mainMenu = new Intent(this,MainMenuAcitivity.class);
        researchActivity = new ResearchActivity();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name.setText(complexe.getLibellé());
        address.setText(complexe.getAdresse());
        if(complexe.getSiteWeb()==null)
            webSite.setVisibility(View.INVISIBLE);
        else {
            webSite.setText(complexe.getSiteWeb());
            webSite.setOnClickListener(onClickWebSite());
        }

        addDispo.setOnClickListener(onClickCreateNewDispo());

        setSportView();
    }

    private View.OnClickListener onClickCreateNewDispo(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportsSélectionnés = researchActivity.getSportsSelectionnés(sportsPraticlables);
                if(sportsSélectionnés.isEmpty()){
                    Toast.makeText(ComplexeProfilActivity.this, "Aucun sport sélectionné", Toast.LENGTH_SHORT).show();
                }
                else{
                    new AsyncNewDispo().execute();
                    startActivity(mainMenu);
                }
            }
        };
    }

    private View.OnClickListener onClickWebSite(){

        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webSite.getText().toString()));
                startActivity(browserIntent);
            }
        };
    }

    private void setSportView(){
        ArrayList<String> sports = complexe.getSports();
        CheckBox sportView;
        for(String sport : sports){
            sportView = new CheckBox(this);
            sportView.append(sport);
            sportsPraticlables.addView(sportView);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(complexe.getCoorX(), complexe.getCoorY()))
                .title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(complexe.getCoorX(), complexe.getCoorY()),15));
    }

    private class AsyncNewDispo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            DisponibiliteDTO dispo= new DisponibiliteDTO();;
            dispo.setUsername(preferences.getString("username",null));
            dispo.setComplexeSportif(complexe.getLibellé());
            DisponibilitéDAO disponibilitéDAO;
            try {
                for(String sport : sportsSélectionnés){
                    disponibilitéDAO = new DisponibilitéDAO(preferences.getString("token",null));
                    dispo.setLibelléSport(sport);
                    disponibilitéDAO.post(dispo);
                }
            }
            catch (Exception e) {}
            return null;
        }
    }
}
