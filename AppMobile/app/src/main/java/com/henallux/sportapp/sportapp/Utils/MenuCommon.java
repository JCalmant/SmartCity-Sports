package com.henallux.sportapp.sportapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.henallux.sportapp.sportapp.AllChatRoomActivity;
import com.henallux.sportapp.sportapp.ContactActivity;
import com.henallux.sportapp.sportapp.MainMenuAcitivity;
import com.henallux.sportapp.sportapp.R;
import com.henallux.sportapp.sportapp.ResearchActivity;
import com.henallux.sportapp.sportapp.SettingsActivity;

/**
 * Created by fran on 10-12-17.
 */

public class MenuCommon {
    private Context context;
    private Intent contact,message,home,research,settings;
    public MenuCommon(Context context){
        this.context=context;
        contact = new Intent(context, ContactActivity.class);
        message= new Intent(context, AllChatRoomActivity.class);
        settings = new Intent(context, SettingsActivity.class);
        home = new Intent(context, MainMenuAcitivity.class);
        research=new Intent(context, ResearchActivity.class);
    }


    public boolean optionsItemSelect(MenuItem item){
        switch(item.getItemId()){
             case R.id.ContactMenu:
                context.startActivity(contact);
                return true;
             case R.id.MessageMenu:
                context.startActivity(message);
                return true;
             case R.id.SettingsMenu:
                context.startActivity(settings);
                return true;
             case R.id.HomeMenu:
                context.startActivity(home);
                return true;
             case R.id.ResearchMenu:
                context.startActivity(research);
                return true;
             default:
                return false;
        }
    }
}
