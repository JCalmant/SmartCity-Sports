package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.sportapp.DataAccess.RecupToken;
import com.squareup.picasso.Picasso;

public class LogInActivity extends AppCompatActivity {
    private Button connectionButton;
    private EditText userName, password;
    private TextView inscritpion;
    private Intent mainMenu, inscritpionMenu;
    private ProgressBar chargement;
    private SharedPreferences preferences;
    private ImageView homeImage;
    private boolean tokenOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_log_in);

        userName=(EditText) findViewById(R.id.userNameRegister);
        password=(EditText) findViewById(R.id.passwordRegister);
        connectionButton = (Button) findViewById(R.id.loginButton);
        inscritpion = (TextView) findViewById(R.id.inscriptionLink);
        inscritpion.setMovementMethod(LinkMovementMethod.getInstance());
        chargement = (ProgressBar) findViewById(R.id.connectionBar);
        homeImage = (ImageView) findViewById(R.id.HomeImage);
        Picasso.with(this).load("https://res.cloudinary.com/dz1qqniju/image/upload/v1513417643/logoApp_cvgbli.jpg").into(homeImage);
        chargement.getIndeterminateDrawable().setColorFilter(Color.rgb(255,165,0), PorterDuff.Mode.MULTIPLY);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        userName.requestFocus();

        mainMenu=new Intent(LogInActivity.this,MainMenuAcitivity.class);
        inscritpionMenu=new Intent(LogInActivity.this,InscriptionAcitivity.class);

        connectionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncConnection  asyncConnection = new AsyncConnection();
                asyncConnection.execute();
            }
        });

        inscritpion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(inscritpionMenu);
            }
        });
    }

    private class AsyncConnection extends AsyncTask<Void, Void, Void>{
        protected Void doInBackground(Void ... v){
            try {
                String token = RecupToken.recupToken().getToken(userName.getText().toString(), password.getText().toString());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token",token);
                editor.putString("username",userName.getText().toString());
                editor.apply();
                tokenOk = true;
            }
            catch (Exception e){
                tokenOk = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if(tokenOk) {
                startActivity(mainMenu);
            }
            else {
                Toast.makeText(LogInActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
            chargement.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chargement.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
