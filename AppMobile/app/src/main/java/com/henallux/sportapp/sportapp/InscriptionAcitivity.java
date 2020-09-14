package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.henallux.sportapp.DataAccess.Inscription;
import com.henallux.sportapp.DataAccess.RecupToken;
import com.henallux.sportapp.Exception.EmptyFieldException;
import com.henallux.sportapp.Exception.FormatException;
import com.henallux.sportapp.Model.User;

/**
 * Created by fran on 03-12-17.
 */

public class InscriptionAcitivity extends AppCompatActivity {
    private Button register;
    private EditText username, password, passwordConfirmation, email;
    private SharedPreferences preferences;
    private Intent editProfil;
    private ProgressBar progressBar;
    private int responseCode;
    private boolean tokenOk, isAllFieldOk;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        register = (Button) findViewById(R.id.registerButton);
        username = (EditText)findViewById(R.id.userNameRegister);
        password = (EditText)findViewById(R.id.passwordRegister);
        passwordConfirmation = (EditText)findViewById(R.id.passwordConfirmation);
        email = (EditText)findViewById(R.id.email);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        progressBar = (ProgressBar) findViewById(R.id.chargementsignup);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255,165,0), PorterDuff.Mode.MULTIPLY);
        user = new User();

        editProfil = new Intent(InscriptionAcitivity.this,EditProfilActivity.class);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isAllFieldOk = true;
                try {
                    user.setUserName(username.getText().toString());
                } catch (EmptyFieldException e) {
                    isAllFieldOk=false;
                    username.setError("Nom d'utilisateur vide");
                }
                try {
                    user.setPassword(password.getText().toString());
                } catch (FormatException e) {
                    isAllFieldOk=false;
                    password.setError("Le mot de passe ne respect pas le format (8 caractères dont 1 maj, 1 chiffre et 1 caractère spécial minimum)");
                } catch (EmptyFieldException e) {
                    isAllFieldOk=false;
                    password.setError("Mot de passe vide");
                }

                try {
                    user.setEmail(email.getText().toString());
                } catch (FormatException e) {
                    isAllFieldOk=false;
                    email.setError("Email invalide");
                } catch (EmptyFieldException e) {
                    isAllFieldOk=false;
                    email.setError("Email vide");
                }
                if(isAllFieldOk && password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                    AsyncInscritpion inscritpion = new AsyncInscritpion();
                    inscritpion.execute();
                }
                else{
                    if(!password.getText().toString().equals(passwordConfirmation.getText().toString()))
                        Toast.makeText(InscriptionAcitivity.this,"Mots de passe différents", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class AsyncInscritpion extends AsyncTask<Void, Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Void ... v){
            try {
                Inscription inscription =new Inscription(username.getText().toString(),password.getText().toString(),email.getText().toString());
                responseCode = inscription.sendInscription();
                String token = RecupToken.recupToken().getToken(username.getText().toString(), password.getText().toString());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token",token);
                editor.putString("username",username.getText().toString());
                editor.apply();
                tokenOk=true;
            }
            catch (Exception e){
                tokenOk=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(tokenOk) {
                if(responseCode==201) {
                    startActivity(editProfil);
                }
                else{
                    Toast.makeText(InscriptionAcitivity.this,"Nom d'utilisateur indisponible", Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}