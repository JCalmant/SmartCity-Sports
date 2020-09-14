package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.henallux.sportapp.sportapp.Utils.MenuCommon;

/**
 * Created by fran on 17-12-17.
 */

public class SettingsActivity extends AppCompatActivity {
    private Button deco;
    private Intent logInPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        logInPage = new Intent(this,LogInActivity.class);
        deco = (Button) findViewById(R.id.Disconnection);

        deco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(logInPage);
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
}
