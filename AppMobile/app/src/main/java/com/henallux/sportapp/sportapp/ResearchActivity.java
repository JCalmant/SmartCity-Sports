package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.henallux.sportapp.DataAccess.ComplexeSportifDAO;
import com.henallux.sportapp.DataAccess.RecupToken;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.Model.ComplexeSportif;
import com.henallux.sportapp.Model.DTO.ComplexeSportifDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 10-12-17.
 */

public class ResearchActivity extends AppCompatActivity {
    private ArrayList<String> sports,sportsSelectionnés,complexeSportifs;
    private ArrayList<UserDTO> userDTOS;
    private UserDAO userDAO;
    private Button sendResearch;
    private LinearLayout choixSports, choixComplexe;
    private SharedPreferences preferences;
    private Intent resultatRecherche;
    private CheckBox filtreComplexe;
    private ScrollView scrollComplexe;
    private String complexeChoisi;
    private ComplexeSportifDTO complexeSportifDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recherche_activity);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sendResearch = (Button) findViewById(R.id.LancerRecherche);
        choixSports = (LinearLayout)findViewById(R.id.RechercheSport);
        choixComplexe = (LinearLayout)findViewById(R.id.RechercheParComplexe);
        filtreComplexe = (CheckBox) findViewById(R.id.OkComplexeBox);
        resultatRecherche = new Intent(this,ResultResearchActivity.class);
        scrollComplexe = (ScrollView) findViewById(R.id.ScrollComplexe);

        filtreComplexe.setOnClickListener(onClickShowComplexe());
        new AsyncListViewAdaptation().execute();

        sendResearch.setOnClickListener(onClickValidateSportSelection());
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

    private View.OnClickListener onClickShowComplexe(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filtreComplexe.isChecked()){
                    scrollComplexe.setVisibility(ScrollView.VISIBLE);
                    new AsyncComplexeView().execute();
                }
                else{
                    scrollComplexe.setVisibility(ScrollView.INVISIBLE);
                    complexeChoisi=null;
                    new AsyncListViewAdaptation().execute();
                }
            }
        };
    }

    private View.OnClickListener onClickValidateSportSelection(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportsSelectionnés = getSportsSelectionnés(choixSports);
                if(sportsSelectionnés.isEmpty()){
                    Toast.makeText(ResearchActivity.this, "Aucun sport sélectionné", Toast.LENGTH_SHORT).show();
                }
                else{
                    new AsyncResearchAmi().execute();
                }
            }
        };
    }

    public ArrayList<String> getSportsSelectionnés(LinearLayout choixSports){
        ArrayList<String> sportsSelection=new ArrayList<>();
        for(int i=0;i<choixSports.getChildCount();i++){
            if(((CheckBox)choixSports.getChildAt(i)).isChecked())
                sportsSelection.add(((CheckBox) choixSports.getChildAt(i))
                        .getText()
                        .toString());
        }
        return sportsSelection;
    }

    private class AsyncListViewAdaptation extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void ... v){
            try {
                userDAO = new UserDAO(preferences.getString("token",null));
                sports = userDAO.getSportsUser(preferences.getString("username",null));
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            setSportView(sports);
        }
    }

    public void setSportView(ArrayList<String> sports){
        choixSports.removeAllViews();
        for(String sport : sports){
            CheckBox checkBox = new CheckBox(ResearchActivity.this);
            checkBox.append(sport);
            choixSports.addView(checkBox);
        }
    }

    private class AsyncResearchAmi extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... v) {
            try{
                userDAO = new UserDAO(preferences.getString("token",null));
                userDTOS = userDAO.getUserBySport(sportsSelectionnés,preferences.getString("username",null));
            }
            catch(Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if(complexeChoisi!=null){
                ArrayList<UserDTO> users = new ArrayList<>();
                for(UserDTO userDTO : userDTOS)
                {
                        int i = 0;
                        while (i < userDTO.getDisponibilites().size() &&
                                (userDTO.getDisponibilites().get(i).getComplexeSportif()==null ||
                                !userDTO.getDisponibilites().get(i).getComplexeSportif().equals(complexeChoisi))) {
                            i++;
                        }
                        if (i < userDTO.getDisponibilites().size() && userDTO.getDisponibilites().get(i).getComplexeSportif()!=null) {
                            users.add(userDTO);
                        }
                }
                resultatRecherche.putExtra("UsersFromResearch",users);
            }
            else{
                resultatRecherche.putExtra("UsersFromResearch",userDTOS);
            }
            startActivity(resultatRecherche);
        }
    }

    private class AsyncComplexeView extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userDAO = new UserDAO(preferences.getString("token",null));
                complexeSportifs = userDAO.getAllComplexeUser(preferences.getString("username",null));
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setComplexeView();
        }
    }

    private void setComplexeView(){
        RadioButton radioButton;
        RadioGroup group = new RadioGroup(this);
        choixComplexe.removeAllViews();
        for(String complexe : complexeSportifs){
            radioButton = new RadioButton(this);
            radioButton.append(complexe);
            radioButton.setOnClickListener(onCheckRadioButton(complexe));
            group.addView(radioButton);
        }
        choixComplexe.addView(group);
    }

    public View.OnClickListener onCheckRadioButton(final String complexe){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complexeChoisi = complexe;
                new AsyncComplexeInfo().execute();
            }

        };
    }

    private class AsyncComplexeInfo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ComplexeSportifDAO complexeSportifDAO = new ComplexeSportifDAO(preferences.getString("token",null));
                complexeSportifDTO = complexeSportifDAO.getComplexeByName(complexeChoisi);
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setSportView(complexeSportifDTO.getSports());
        }
    }
    @Override
    public void onBackPressed() {
    }

}
