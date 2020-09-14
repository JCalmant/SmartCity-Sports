package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henallux.sportapp.DataAccess.GroupeDAO;
import com.henallux.sportapp.Model.DTO.GroupeDTO;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;

import java.util.ArrayList;

/**
 * Created by fran on 17-12-17.
 */

public class AllChatRoomActivity extends AppCompatActivity {
    private LinearLayout chatRoomsCaintainer;
    private ArrayList<GroupeDTO> userGroups;
    private SharedPreferences preferences;
    private String username;
    private Intent chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_recherche_activity);
        chatRoomsCaintainer = (LinearLayout)findViewById(R.id.LinearLayoutReaserch);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString("username",null);
        new AsyncGetAllGroups().execute();
        chat = new Intent(this,TchatActivity.class);
    }

    public void setLayout(){
        TextView textView;
        LinearLayout.LayoutParams marge = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chatRoomsCaintainer.setPadding(0,0,0,0);
        for(GroupeDTO groupe : userGroups){
            StringBuilder roomName = new StringBuilder();
            textView = new TextView(this);
            textView.setTextSize(20);
            textView.setBackground(getDrawable(R.drawable.border_image));
            textView.setLayoutParams(marge);
            textView.setOnClickListener(onClickChatRoom(groupe));
            for(String membre : groupe.getMembre())
                roomName.append(membre+", ");
            roomName.deleteCharAt(roomName.length()-2);
            textView.setText(roomName);
            chatRoomsCaintainer.addView(textView);
        }
    }

    public View.OnClickListener onClickChatRoom(final GroupeDTO groupeDTO){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat.putExtra("salon",groupeDTO);
                startActivity(chat);
            }
        };
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

    private class AsyncGetAllGroups extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                GroupeDAO groupeDAO = new GroupeDAO(preferences.getString("token",null));
                userGroups = groupeDAO.getAllUserGroupes(username);
            } catch (Exception e) {
                userGroups = new ArrayList<>();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setLayout();
        }
    }
}