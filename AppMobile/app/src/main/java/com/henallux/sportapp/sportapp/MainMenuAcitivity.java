package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.User;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 02-12-17.
 */

public class MainMenuAcitivity extends AppCompatActivity {
    private ImageView photoProfil;
    private TextView editProfil;
    private Button salleSportButton;
    private Intent resultSelectionComplexePage, editProfilPage, firstEditProfil;
    private UserDTO userDTO;
    private UserDAO userDAO;
    private SharedPreferences preferences;
    private ArrayList<String> userSports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        new AsynTaskGetUser().execute();
        photoProfil = (ImageView) findViewById(R.id.PhotoAcceuil);
        salleSportButton = (Button) findViewById(R.id.SalleSportMatch);
        resultSelectionComplexePage = new Intent(this,ResultResearchActivity.class);
        editProfilPage = new Intent(this,EditProfilFromMainMenuActivity.class);
        editProfil = (TextView) findViewById(R.id.EditProfilAcceuil);
        firstEditProfil = new Intent(this,EditProfilActivity.class);

        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfilPage.putExtra("user",userDTO);
                startActivity(editProfilPage);
            }
        });

        salleSportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(resultSelectionComplexePage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return new MenuCommon(this).optionsItemSelect(item);
    }

    private class AsynTaskGetUser extends AsyncTask <Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                userDAO = new UserDAO(preferences.getString("token",null));
                userDTO = userDAO.getByUsername(preferences.getString("username",null));
                userSports =userDAO.getSportsUser(userDTO.getUsername());
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id",userDTO.getId());
            editor.apply();
            if(userDTO.getDateNaissance()==null){
                startActivity(firstEditProfil);
            }
            else {
                Picasso.with(MainMenuAcitivity.this).load(userDTO.getPhoto()).into(photoProfil);
                resultSelectionComplexePage.putExtra("SportsFromUser", userSports);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
