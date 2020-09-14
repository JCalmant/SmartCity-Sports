package com.henallux.sportapp.sportapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.henallux.sportapp.DataAccess.SportDAO;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.DataAccess.Utils.CloudinaryAccess;
import com.henallux.sportapp.Exception.EmptyFieldException;
import com.henallux.sportapp.Model.Disponibilite;
import com.henallux.sportapp.Model.Sport;
import com.henallux.sportapp.Model.User;
import com.henallux.sportapp.sportapp.NewComponent.MultiSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fran on 04-12-17.
 */

public class EditProfilActivity extends AppCompatActivity {
    private Intent mainMenu, chooseDate;
    private ImageButton sendInfo;
    private Spinner sexe;
    private MultiSpinner sportsAffiché;
    private SharedPreferences preferences;
    private EditText  profession,aboutMe, naissance;
    private ImageView photoProfil;
    private CloudinaryAccess cloudinaryAccess;
    private Date dateNaissance;
    private User user;
    private ArrayList<Sport> sports;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_edit_profil_activity);

        cloudinaryAccess = CloudinaryAccess.CloudinaryAccess(EditProfilActivity.this);

        mainMenu = new Intent(EditProfilActivity.this, MainMenuAcitivity.class);
        chooseDate = new Intent(EditProfilActivity.this, ChooseDateActivity.class);

        sendInfo = (ImageButton) findViewById(R.id.sendInfo);
        sexe = (Spinner) findViewById(R.id.spinnerSexe);
        sportsAffiché = (MultiSpinner) findViewById(R.id.spinnerSport);
        naissance = (EditText) findViewById(R.id.naissance);
        profession = (EditText) findViewById(R.id.professionText);
        aboutMe = (EditText) findViewById(R.id.aboutMeText);
        photoProfil = (ImageView) findViewById(R.id.photoUtilisateur);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setNaissance();

        String[] sexes = new String[]{"Homme", "Femme"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sexes);
        sexe.setAdapter(adapter);

        new GetSportsAsync().execute();

        photoProfil.setOnClickListener(onClickPhotoProfil());

        naissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate.putExtra("user","");
                startActivity(chooseDate);
            }
        });

        sendInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    setUser();
                } catch (EmptyFieldException e) {}
                new PutUserInfoAsync().execute();
            }
        });
    }

    public View.OnClickListener onClickPhotoProfil(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        photoToCloudinary(this,photoProfil,cloudinaryAccess, data, resultCode, requestCode);

    }

    public void photoToCloudinary(Context context, ImageView imageView, CloudinaryAccess cloudinaryAccess, Intent data ,int resultCode,int requestCode){
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData()));
                        cloudinaryAccess.setPhotoUri(data.getData());
                        cloudinaryAccess.uploadPhoto();
                    }
                    catch (IOException e){}
                }
            }
        }
    }

    private class PutUserInfoAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userDAO =new UserDAO(preferences.getString("token",null));
                userDAO.put(user);
            } catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(mainMenu);
        }
    }

    private class GetSportsAsync extends AsyncTask<Void, Void, ArrayList<Sport>> {
        protected ArrayList<Sport> doInBackground(Void ... v){
            try {
                SportDAO recupSport = new SportDAO(preferences.getString("token",null));
                return recupSport.getAllSport();
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Sport> sportsList) {
            super.onPostExecute(sportsList);
            sports = sportsList;
            final ArrayList sportsListSpinner = new ArrayList<String>();
            for(Sport sport : sports){
                sportsListSpinner.add(sport.toString());
            }
            sportsAffiché.setItems(sportsListSpinner);
        }
    }

    private void setNaissance(){
        naissance=(EditText)findViewById(R.id.naissance);
        dateNaissance = new Date(getIntent().getLongExtra("dateNaissance", Long.parseLong("1512728434460")));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateNaissance);
        naissance.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
    }

    public void setUser() throws EmptyFieldException {
        user = new User();
        user.setUserName(preferences.getString("username",null));
        user.setAboutMe(aboutMe.getText().toString());
        user.setDateNaissance(dateNaissance);
        user.setProfession(profession.getText().toString());
        if(cloudinaryAccess.getUrlPhoto()!=null)
            user.setPhoto(cloudinaryAccess.getUrlPhoto());
        if(sexe.getSelectedItem().toString().equals("Femme")){
            user.setSexe(User.FEMME);
        }
        else{
            user.setSexe(User.HOMME);
        }

        for(String sport : sportsAffiché.getSelectedStrings()){
            user.getDisponibilites().add(new Disponibilite(sports.get(sportsAffiché.getIndex(sport)-1),null,user));
        }
    }

    @Override
    public void onBackPressed() {
    }
}