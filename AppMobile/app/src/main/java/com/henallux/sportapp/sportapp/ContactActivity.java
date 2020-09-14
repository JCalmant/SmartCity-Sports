package com.henallux.sportapp.sportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.henallux.sportapp.DataAccess.GroupeDAO;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.Model.DTO.AmiDTO;
import com.henallux.sportapp.Model.DTO.GroupeDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.User;
import com.henallux.sportapp.sportapp.Utils.CommonAsyncTaskListProfil;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 12-12-17.
 */

public class ContactActivity extends AppCompatActivity {
    private TabHost.TabSpec tab1,tab2,tab3;
    private TabHost host;
    private LinearLayout amis,ajouteur,ajouté;
    private UserDAO userDAO;
    private UserDTO userDTO;
    private SharedPreferences preferences;
    private Button add,remove,conversation;
    private ArrayList<UserDTO> listAmis,listAjouté,listAjouteur;
    private CommonAsyncTaskListProfil commonAsyncTaskListProfil;
    private ProgressBar progressBar;
    private ArrayList<GroupeDTO> userGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        amis = (LinearLayout) findViewById(R.id.ScrollAmis);
        ajouteur = (LinearLayout)findViewById(R.id.ScrollAjouteur);
        ajouté = (LinearLayout) findViewById(R.id.ScrollAjouté);
        progressBar = (ProgressBar) findViewById(R.id.ProgressContact);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        add = new Button(this);
        remove = new Button(this);
        conversation = new Button(this);

        add.setText(R.string.Add);
        remove.setText(R.string.Remove);
        conversation.setText(R.string.Conversation);
        tabGeneration();
        new AsyncUserInfosLoading().execute();
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

    public void tabGeneration(){
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        tab1 = host.newTabSpec("Tab One");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Amis");
        host.addTab(tab1);

        //Tab 2
        tab2 = host.newTabSpec("Tab Two");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Ils veulent vous ajouter");
        host.addTab(tab2);

        //Tab 3
        tab3 = host.newTabSpec("Tab Three");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("Vous voulez les ajouter");
        host.addTab(tab3);
    }

    private LinearLayout miseEnPlaceLayout(LinearLayout tab, ArrayList<UserDTO> users, Context context, Button ... views) {
        if(users!=null) {
            for (UserDTO userDTO : users) {
                if(!userDTO.getUsername().equals(preferences.getString("username",null))) {
                    tab.addView(commonAsyncTaskListProfil.userListForm(userDTO,views));
                }
            }
        }
        return tab;
    }

    public void getAmis(UserDTO userDTO) throws IOException, JSONException {
        listAmis = new ArrayList<>();
        listAjouté = new ArrayList<>();
        listAjouteur = new ArrayList<>();
        UserDTO userAjouté;
        UserDTO userAjouteur;
        for(AmiDTO ami : userDTO.getAmis()){
            userAjouté = userDAO.getByUsername(ami.getAmiAjouté());
            userAjouteur = userDAO.getByUsername(ami.getAmiAjouteur());
            if(ami.isAccepté()){
                if (ami.getAmiAjouté().equals(preferences.getString("username",null))){
                    listAmis.add(userAjouteur);
                }
                else{
                    listAmis.add(userAjouté);
                }
            }
            else{
                if(ami.getAmiAjouté().equals(preferences.getString("username",null))){
                    listAjouteur.add(userAjouteur);
                }
                else{
                    listAjouté.add(userAjouté);
                }
            }
        }
    }

    private class AsyncFriendshipLoading extends AsyncTask<Void,Void,Void>{


        protected Void doInBackground(Void ... v){
            try {
                GroupeDAO groupeDAO = new GroupeDAO(preferences.getString("token",null));
                userGroups = groupeDAO.getAllUserGroupes(userDTO.getUsername());
                getAmis(userDTO);


            }
            catch(Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            commonAsyncTaskListProfil = new CommonAsyncTaskListProfil(ContactActivity.this,userGroups);
            amis = miseEnPlaceLayout(amis,listAmis,ContactActivity.this,conversation,remove);
            ajouté = miseEnPlaceLayout(ajouté,listAjouté,ContactActivity.this);
            ajouteur = miseEnPlaceLayout(ajouteur,listAjouteur,ContactActivity.this,add,remove);
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }
    private class AsyncUserInfosLoading extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userDAO = new UserDAO(preferences.getString("token",null));
                userDTO = userDAO.getByUsername(preferences.getString("username",null));
            }
            catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!userDTO.getAmis().isEmpty()) {
                new AsyncFriendshipLoading().execute();
            }
            else {
                progressBar.setVisibility(ProgressBar.GONE);
            }
        }
    }
}