package com.henallux.sportapp.sportapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henallux.sportapp.DataAccess.AmitiésDAO;
import com.henallux.sportapp.DataAccess.GroupeDAO;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.DataAccess.Utils.RequestMaker;
import com.henallux.sportapp.Model.Amitié;
import com.henallux.sportapp.Model.DTO.AmiDTO;
import com.henallux.sportapp.Model.DTO.GroupeDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.Model.User;
import com.henallux.sportapp.sportapp.ContactActivity;
import com.henallux.sportapp.sportapp.PublicProfilActivity;
import com.henallux.sportapp.sportapp.R;
import com.henallux.sportapp.sportapp.ResultResearchActivity;
import com.henallux.sportapp.sportapp.TchatActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fran on 13-12-17.
 */

public class CommonAsyncTaskListProfil {
    private Intent publicProfil, tchat;
    private Context context;
    private SharedPreferences preferences;
    private AmitiésDAO amitiésDAO;
    private AmiDTO amiAModif;
    private ArrayList<GroupeDTO> userGroups;
    public CommonAsyncTaskListProfil(Context context, ArrayList<GroupeDTO> userGroups){
        this.context=context;
        if(userGroups!=null)this.userGroups=userGroups;
        this.publicProfil=new Intent(context, PublicProfilActivity.class);
        this.tchat=new Intent(context,TchatActivity.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        amiAModif = new AmiDTO();
        try {
            amitiésDAO = new AmitiésDAO(preferences.getString("token",null));
        } catch (Exception e) {}
    }

    public LinearLayout userListForm(UserDTO userDTO, Button[] views){
        LinearLayout containerProfil;
        LinearLayout profilInfos;
        FrameLayout containerPhoto;
        LinearLayout.LayoutParams dimensionPhoto = new LinearLayout.LayoutParams(300,300);
        LinearLayout.LayoutParams marge = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        marge.setMargins(80,45,0,0);
        TextView nom;
        TextView sexe;
        TextView age;
        ImageView photoProfil;

        nom = new TextView(context);
        nom.setText(userDTO.getUsername());
        sexe = new TextView(context);
        sexe.setText(userDTO.getSexe()== User.HOMME?"Homme":"Femme");
        age = new TextView(context);
        age.setText(String.valueOf(userDTO.getAge())+" ans");
        nom.setTextColor(context.getResources().getColor(R.color.pruneFoncé));
        sexe.setTextColor(context.getResources().getColor(R.color.pruneFoncé));
        age.setTextColor(context.getResources().getColor(R.color.pruneFoncé));
        photoProfil = new ImageView(context);
        Picasso.with(context).load((userDTO.getPhoto())).into(photoProfil);
        photoProfil.setOnClickListener(onClickPhoto(userDTO.getUsername()));
        photoProfil.setBackground(context.getDrawable(R.drawable.border_image));
        containerPhoto = new FrameLayout(context);
        containerPhoto.addView(photoProfil);
        containerPhoto.setLayoutParams(dimensionPhoto);
        profilInfos = new LinearLayout(context);
        profilInfos.setLayoutParams(marge);
        profilInfos.setOrientation(LinearLayout.VERTICAL);
        profilInfos.addView(nom);
        profilInfos.addView(sexe);
        profilInfos.addView(age);
        containerProfil = new LinearLayout(context);
        containerProfil.setOrientation(LinearLayout.HORIZONTAL);
        containerProfil.addView(containerPhoto);
        containerProfil.addView(profilInfos);
        if(views!=null && views.length!=0)containerProfil.addView(addComponent(views,userDTO));
        return containerProfil;
    }

    public LinearLayout addComponent(Button[] views, UserDTO userDTO){
        LinearLayout.LayoutParams marge = new LinearLayout.LayoutParams(300,130);
        marge.setMargins(80,10,0,10);
        LinearLayout buttonContainer = new LinearLayout(context);
        buttonContainer.setOrientation(LinearLayout.VERTICAL);
        Button transition;
        for( Button v : views){
            transition = new Button(v.getContext());
            transition.setText(v.getText());
            transition.setLayoutParams(marge);
            transition.setOnClickListener(onClickAddRemoveConversation(userDTO, v.getText().toString()));
            buttonContainer.addView(transition);
        }
        return buttonContainer;
    }

    private View.OnClickListener onClickAddRemoveConversation(final UserDTO userDTO, final String buttonText){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonText.equals("Ajouter")){
                    amiAModif.setAmiAjouté(preferences.getString("username",null));
                    amiAModif.setAmiAjouteur(userDTO.getUsername());
                    amiAModif.setAccepté(Amitié.AMI);

                    new AsyncGestionButton().execute(RequestMaker.PUT);
                }
                else{
                    if(buttonText.equals("Supprimer")){
                        amiAModif = userDTO.getAmitié(userDTO.getUsername(),preferences.getString("username",null));

                        new AsyncGestionButton().execute(RequestMaker.DELETE);
                    }
                    else {
                        String wantToChatWith = userDTO.getUsername();
                        GroupeDTO salon=new GroupeDTO();
                        salon.setGroupeId(0);
                        int i =0;
                        while(i<userGroups.size()){
                            if(userGroups.get(i).getMembre().size()==1) {
                                if(userGroups.get(i).getMembre().get(0).equals(wantToChatWith)){
                                    salon = userGroups.get(i);
                                    i = userGroups.size();
                                }
                            }
                            i++;
                        }
                        if(salon.getGroupeId()==0){
                            new AsyncCreationSalon().execute(userDTO);
                        }
                        else {
                            tchat.putExtra("salon", salon);
                            context.startActivity(tchat);
                        }
                    }
                }
            }
        };
    }

    private View.OnClickListener onClickPhoto(final String username){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncProfilLoading().execute(username);
            }
        };
    }

    private class AsyncProfilLoading extends android.os.AsyncTask<String, Void, UserDTO> {
        protected UserDTO doInBackground(String ... username){
            UserDTO userDTO=null;
            try {
                UserDAO userDAO = new UserDAO(preferences.getString("token",null));
                userDTO = userDAO.getByUsername(username[0]);
            }
            catch (Exception e){}
            return userDTO;
        }

        @Override
        protected void onPostExecute(UserDTO userDTO) {
            publicProfil.putExtra("UserProfil",userDTO);
            context.startActivity(publicProfil);
        }
    }

    private class AsyncGestionButton extends AsyncTask <Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer ... info) {
            try{
                switch (info[0]){
                    case RequestMaker.DELETE:
                        amitiésDAO.delete(amiAModif);
                        break;
                    case RequestMaker.PUT:
                        amitiésDAO.put(amiAModif);
                        break;
                    default:
                }
            }
            catch(Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent= new Intent(context, ContactActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        }
    }

    private class AsyncCreationSalon extends AsyncTask <UserDTO,Void,GroupeDTO>{

        @Override
        protected GroupeDTO doInBackground(UserDTO... users) {
            GroupeDTO groupe = new GroupeDTO();
            try {
                GroupeDAO groupeDAO= new GroupeDAO(preferences.getString("token",null));
                groupe.setGroupeId(0);
                groupe.getMembre().add(users[0].getUsername());
                groupe.getMembre().add(preferences.getString("username",null));
                groupe.setGroupeId(groupeDAO.post(groupe));
            } catch (Exception e) {}
            return groupe;
        }

        @Override
        protected void onPostExecute(GroupeDTO i) {
            super.onPostExecute(i);
            if(i.getGroupeId()!=0){
                tchat.putExtra("salon", i);
                context.startActivity(tchat);
            }
        }
    }
}