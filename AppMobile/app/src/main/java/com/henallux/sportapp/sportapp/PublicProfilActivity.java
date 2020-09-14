package com.henallux.sportapp.sportapp;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.henallux.sportapp.DataAccess.AmitiésDAO;
import com.henallux.sportapp.DataAccess.RecupToken;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.Model.Amitié;
import com.henallux.sportapp.Model.DTO.AmiDTO;
import com.henallux.sportapp.Model.DTO.DisponibiliteDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.User;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 09-12-17.
 */

public class PublicProfilActivity extends AppCompatActivity {
    private ImageView photo;
    private TextView age, aboutMe, profession, username;
    private ScrollView panneauSport;
    private ImageButton addOrRemove;
    private UserDTO userDTO;
    private SharedPreferences preferences;
    private boolean isAmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_profil_activity);
        photo = (ImageView) findViewById(R.id.PhotoPublicProfil);
        age = (TextView) findViewById(R.id.AgePublicProfil);
        aboutMe = (TextView) findViewById(R.id.AboutMePublicProfil);
        profession = (TextView) findViewById(R.id.ProfessionPublicProfil);
        username = (TextView) findViewById(R.id.UserNamePublicProfil);
        panneauSport = (ScrollView) findViewById(R.id.GridSport);
        addOrRemove = (ImageButton) findViewById(R.id.AddOrRemoveFriend);
        addOrRemove.setOnClickListener(addOrRemoveListener());
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userDTO = (UserDTO) getIntent().getSerializableExtra("UserProfil");
        loadInfos();
    }

    private View.OnClickListener addOrRemoveListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncFriendManager().execute();
            }
        };
    }


    private void loadInfos() {
        Picasso.with(PublicProfilActivity.this).load((userDTO.getPhoto())).into(photo);
        age.setText(String.valueOf(userDTO.getAge())+" ans");
        aboutMe.setText(userDTO.getAboutMe()); ;
        profession.setText(userDTO.getProfession());
        username.setText(userDTO.getUsername());
        setArrayListSport();
        if(userDTO.getAmis()!=null){
            setFriendOrNot();
        }
        else{
            setButtonAmitiés(Amitié.PASAMI);
        }
    }

    private void setArrayListSport(){
        LinearLayout.LayoutParams marge = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        marge.setMargins(10,10,10,10);
        Button sport;
        ListAdapter adapter;
        LinearLayout layoutVertical=new LinearLayout(this);
        layoutVertical.setOrientation(LinearLayout.VERTICAL);
        LinearLayout layoutHorizontal=null;
        for(int i= 0; i< userDTO.getDisponibilites().size();i++){
            int j=0;
            layoutHorizontal = new LinearLayout(this);
            layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            while(j<3 && i< userDTO.getDisponibilites().size()) {
                sport = new Button(PublicProfilActivity.this);
                sport.setLayoutParams(marge);
                sport.append(userDTO.getDisponibilites().get(i).getLibelléSport());
                layoutHorizontal.addView(sport);
                j++;
                i++;
            }
            i--;
            layoutVertical.addView(layoutHorizontal);
        }
        panneauSport.addView(layoutVertical);
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

    public void setFriendOrNot(){
        int i=0;
        while(i<userDTO.getAmis().size()
                &&
                !userDTO.getAmis().get(i).getAmiAjouteur().equals(preferences.getString("username",null))
                &&
                !userDTO.getAmis().get(i).getAmiAjouté().equals(preferences.getString("username",null)))
            i++;
        if(i==userDTO.getAmis().size()){
            setButtonAmitiés(Amitié.PASAMI);
        }
        else{
            setButtonAmitiés(Amitié.AMI);
        }
    }

    public void setButtonAmitiés(boolean isAmi){
        this.isAmi = isAmi;
        if(isAmi){
            addOrRemove.setBackground(getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
            ViewCompat.setBackgroundTintList(addOrRemove, ColorStateList.valueOf(getResources().getColor(R.color.rouge)));
        }
        else{
            addOrRemove.setBackground(getDrawable(android.R.drawable.ic_menu_add));
            ViewCompat.setBackgroundTintList(addOrRemove, ColorStateList.valueOf(getResources().getColor(R.color.vertok)));
        }
    }

    private class AsyncFriendManager extends AsyncTask <Void,Void,Void>{

        protected Void doInBackground(Void ... v){
            try {
                AmitiésDAO amitiésDAO = new AmitiésDAO(preferences.getString("token",null));
                if(!isAmi){
                    amitiésDAO.post(new Amitié(userDTO.getId(),preferences.getString("id",null),Amitié.PASAMI));
                    isAmi = true;
                }
                else{
                    AmiDTO amis = new AmiDTO();
                    amis.setAmiAjouteur(preferences.getString("username",null));
                    amis.setAmiAjouté(userDTO.getUsername());
                    amitiésDAO.delete(amis);
                    isAmi = false;
                }
            }
            catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            PublicProfilActivity.this.setButtonAmitiés(isAmi);
        }
    }
}