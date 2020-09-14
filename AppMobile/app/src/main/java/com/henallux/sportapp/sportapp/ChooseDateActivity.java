package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.henallux.sportapp.DataAccess.RecupToken;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fran on 07-12-17.
 */

public class ChooseDateActivity extends AppCompatActivity {
    private Button confirm;
    private DatePicker datePicker;
    private Intent firstEdit, normalEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_date_activity);

        confirm = (Button) findViewById(R.id.confirmDate);
        datePicker = (DatePicker)findViewById(R.id.datePickerNaissance);
        firstEdit = new Intent (this,EditProfilActivity.class);
        normalEdit = new Intent(this,EditProfilFromMainMenuActivity.class);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GregorianCalendar date = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                if(Years.yearsBetween(new LocalDate(date.getTimeInMillis()), new LocalDate()).getYears()>15){
                    if(getIntent().getSerializableExtra("user")!=null) {
                        firstEdit.putExtra("dateNaissance",date.getTimeInMillis());
                        startActivity(firstEdit);
                    }
                    else{
                        normalEdit.putExtra("dateNaissance",date.getTimeInMillis());
                        normalEdit.putExtra("user",getIntent().getSerializableExtra("userDTO"));
                        startActivity(normalEdit);
                    }
                }
                else{
                    Toast.makeText(ChooseDateActivity.this,"Pas en dessous de 16 ans !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
