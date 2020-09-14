package com.henallux.sportapp.sportapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.henallux.sportapp.DataAccess.DisponibilitéDAO;
import com.henallux.sportapp.DataAccess.SportDAO;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.DataAccess.Utils.CloudinaryAccess;
import com.henallux.sportapp.Exception.EmptyFieldException;
import com.henallux.sportapp.Model.DTO.DisponibiliteDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.Disponibilite;
import com.henallux.sportapp.Model.Sport;
import com.henallux.sportapp.Model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fran on 16-12-17.
 */

public class EditProfilFromMainMenuActivity extends AppCompatActivity {
    private Button edit;
    private EditText naissance,profession,aboutMe;
    private ScrollView pratiqués,nonPratiqués,complexes;
    private ImageView photo;
    private UserDTO user;
    private EditProfilActivity editProfilActivity;
    private CloudinaryAccess cloudinaryAccess;
    private Intent chooseDate, mainMenu;
    private Date dateNaissance;
    private SharedPreferences preferences;
    private ArrayList<String> sportsList, complexesList, noSportsList;
    private ArrayList<Sport> allSport;
    private ProgressDialog progressDialog;
    private User userPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profil);
        edit = (Button)findViewById(R.id.SendEdit);
        photo = (ImageView) findViewById(R.id.ImageEdit);
        naissance = (EditText) findViewById(R.id.NaissanceEdit);
        profession=(EditText)findViewById(R.id.ProfessionEdit);
        aboutMe=(EditText)findViewById(R.id.AboutMeEdit);
        pratiqués = (ScrollView) findViewById(R.id.SportPratiquésEdit);
        nonPratiqués=(ScrollView) findViewById(R.id.SportNonPratiquésEdit);
        complexes=(ScrollView) findViewById(R.id.ComplexeEdit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sauvegarde en cours ...");
        cloudinaryAccess=CloudinaryAccess.CloudinaryAccess(EditProfilFromMainMenuActivity.this);
        chooseDate = new Intent(this,ChooseDateActivity.class);
        mainMenu = new Intent(this,MainMenuAcitivity.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editProfilActivity = new EditProfilActivity();

        user = (UserDTO) getIntent().getSerializableExtra("user");

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        naissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate.putExtra("userDTO",user);
                startActivity(chooseDate);
            }
        });

        setProfil();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncUpdate().execute();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        editProfilActivity.photoToCloudinary(this,photo,cloudinaryAccess, data, resultCode, requestCode);
    }

    private void setProfil(){
        setNaissance();

        profession.setText(user.getProfession());
        aboutMe.setText(user.getAboutMe());
        Picasso.with(EditProfilFromMainMenuActivity.this).load(user.getPhoto()).into(photo);
        sportsList=UserDAO.getAllComplexeOrSport(user,UserDAO.SPORT);
        complexesList=UserDAO.getAllComplexeOrSport(user,UserDAO.COMPLEXE);
        new AsyncGetSportNoPratiqué().execute();
        pratiqués.addView(setLayoutWithCheckBox(sportsList,true));
        complexes.addView(setLayoutWithCheckBox(complexesList,true));
    }

    private void setNaissance(){
        dateNaissance = new Date(getIntent().getLongExtra("dateNaissance", user.getDateNaissance().getTime()));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateNaissance);
        naissance.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
        naissance.setFocusable(false);
    }

    private class AsyncGetSportNoPratiqué extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
               SportDAO sportDAO = new SportDAO(preferences.getString("token",null));
               allSport=sportDAO.getAllSport();
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            noSportsList = new ArrayList<>();
            for(Sport sport : allSport){
                if (!sportsList.contains(sport.toString()))noSportsList.add(sport.toString());
            }
            nonPratiqués.addView(setLayoutWithCheckBox(noSportsList,false));
        }
    }

    private LinearLayout setLayoutWithCheckBox(ArrayList<String> list,boolean isCheck){
        CheckBox checkBox;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for(String item : list){
            checkBox = new CheckBox(this);
            checkBox.append(item);
            checkBox.setChecked(isCheck);
            linearLayout.addView(checkBox);
        }
        return linearLayout;
    }

    private class AsyncUpdate extends AsyncTask<Void,Void,Void>{
        ArrayList<DisponibiliteDTO> post;
        ArrayList<DisponibiliteDTO> delete;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            delete = loadingLists(pratiqués,"deleteSport");
            post = loadingLists(nonPratiqués,"postSport");
            delete.addAll(loadingLists(complexes,"deleteComplexe"));


            try {
                setUser();
            } catch (EmptyFieldException e) {}
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                UserDAO userDAO = new UserDAO(preferences.getString("token",null));
                userDAO.put(userPut);
                for(DisponibiliteDTO dispo : post){
                    DisponibilitéDAO disponibilitéDAO= new DisponibilitéDAO(preferences.getString("token",null));
                    disponibilitéDAO.post(dispo);
                }

                for(DisponibiliteDTO dispo : delete){
                    DisponibilitéDAO disponibilitéDAO= new DisponibilitéDAO(preferences.getString("token",null));
                    disponibilitéDAO.delete(dispo);
                }

            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            startActivity(mainMenu);
        }
    }

    private void setUser() throws EmptyFieldException {
        userPut = new User();
        userPut.setUserName(user.getUsername());
        userPut.setAboutMe(aboutMe.getText().toString());
        userPut.setDateNaissance(dateNaissance);
        userPut.setProfession(profession.getText().toString());
        if(cloudinaryAccess.getUrlPhoto()!=null)
            userPut.setPhoto(cloudinaryAccess.getUrlPhoto());
        userPut.setSexe(user.getSexe());
    }

    private ArrayList<DisponibiliteDTO> loadingLists(ScrollView scrollView, String methode) {
        LinearLayout layout = (LinearLayout)scrollView.getChildAt(0);
        ArrayList<DisponibiliteDTO> list = new ArrayList<>();
        DisponibiliteDTO dispo;
        if(layout!=null){
            for (int i = 0; i < layout.getChildCount(); i++) {
                dispo = new DisponibiliteDTO();
                dispo.setUsername(user.getUsername());
                if (!((CheckBox) layout.getChildAt(i)).isChecked()) {
                    if (methode.equals("deleteSport")) {
                        dispo.setLibelléSport(((CheckBox) layout.getChildAt(i)).getText().toString());
                        list.add(dispo);
                    }
                    else {
                        if(methode.equals("deleteComplexe")) {
                            dispo.setComplexeSportif(((CheckBox) layout.getChildAt(i)).getText().toString());
                            list.add(dispo);
                        }
                    }
                }
                else{
                    if(methode.equals("postSport")) {
                        dispo.setLibelléSport(((CheckBox) layout.getChildAt(i)).getText().toString());
                        list.add(dispo);
                    }
                }
            }
        }
        return list;
    }
}