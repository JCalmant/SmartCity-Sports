package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.henallux.sportapp.DataAccess.ComplexeSportifDAO;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.Model.DTO.ComplexeSportifDTO;
import com.henallux.sportapp.Model.DTO.DisponibiliteDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.User;
import com.henallux.sportapp.sportapp.Utils.CommonAsyncTaskListProfil;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 11-12-17.
 */

public class ResultResearchActivity extends AppCompatActivity {
    private LinearLayout resultat;
    private ArrayList<UserDTO> utilisateursTrouvrés;
    private CommonAsyncTaskListProfil commonAsyncTaskListProfil;
    private ArrayList<ComplexeSportifDTO> complexesTrouvés;
    private ArrayList<String> userSports, complexesChosenYet;
    private ComplexeSportifDAO complexeSportifDAO;
    private SharedPreferences preferences;
    private Intent complexeActivity;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_recherche_activity);
        resultat = (LinearLayout) findViewById(R.id.LinearLayoutReaserch);
        utilisateursTrouvrés = (ArrayList<UserDTO>) getIntent().getSerializableExtra("UsersFromResearch");
        userSports = (ArrayList<String>) getIntent().getSerializableExtra("SportsFromUser");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        complexeActivity = new Intent(this,ComplexeProfilActivity.class);
        if(utilisateursTrouvrés!=null)
            for(UserDTO userDTO : utilisateursTrouvrés) {
                commonAsyncTaskListProfil = new CommonAsyncTaskListProfil(this,null);
                resultat.addView(commonAsyncTaskListProfil.userListForm(userDTO,null));
            }
        else{
            new AsyncLoadingComplexe().execute();
        }
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

    private class AsyncLoadingComplexe extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                complexeSportifDAO = new ComplexeSportifDAO(preferences.getString("token",null));
                complexesTrouvés = complexeSportifDAO.getAllComplexesForXSport(userSports);
                userDAO = new UserDAO(preferences.getString("token",null));
                complexesChosenYet = userDAO.getAllComplexeUser(preferences.getString("username",null));
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                affichageComplexes();
            } catch (Exception e) {}
        }
    }

    private void affichageComplexes() throws IOException, JSONException {
        LinearLayout horizontal;
        RadioButton style;
        TextView libellé;
        for(ComplexeSportifDTO complexe : complexesTrouvés){
            if(complexesChosenYet!=null && !complexesChosenYet.contains(complexe.getLibellé())) {
                horizontal = new LinearLayout(this);
                libellé = new TextView(this);
                style = new RadioButton(this);
                style.setChecked(true);
                style.setClickable(false);
                horizontal.setOrientation(LinearLayout.HORIZONTAL);
                libellé.setTextSize(25);
                libellé.setText(complexe.getLibellé());
                libellé.setOnClickListener(onClickComplexe(complexe));
                horizontal.addView(style);
                horizontal.addView(libellé);
                resultat.addView(horizontal);
            }
        }
    }

    private View.OnClickListener onClickComplexe(final ComplexeSportifDTO complexe){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complexeActivity.putExtra("Complexe",complexe);
                startActivity(complexeActivity);
            }
        };
    }
}