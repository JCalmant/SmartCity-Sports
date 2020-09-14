package com.henallux.sportapp.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.henallux.sportapp.DataAccess.GroupeDAO;
import com.henallux.sportapp.DataAccess.UserDAO;
import com.henallux.sportapp.Model.Amitié;
import com.henallux.sportapp.Model.DTO.AmiDTO;
import com.henallux.sportapp.Model.DTO.GroupeDTO;
import com.henallux.sportapp.Model.DTO.UserDTO;
import com.henallux.sportapp.sportapp.Utils.MenuCommon;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fran on 14-12-17.
 */

public class TchatActivity extends AppCompatActivity {
    private Firebase salon;
    private EditText message;
    private Button envoi;
    private LinearLayout containerMessages;
    private SharedPreferences preferences;
    private String username;
    private ScrollView scrollView,listAmis;
    private ImageButton ajout;
    private ArrayList<AmiDTO> amis;
    private GroupeDTO groupe;
    private ConstraintLayout constraintLayout;
    private boolean visibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tchat_activity);

        groupe = (GroupeDTO) getIntent().getSerializableExtra("salon");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username=preferences.getString("username",null);
        new AsyncGetAllOtherFriends().execute();
        message = (EditText) findViewById(R.id.TextChat);
        envoi = (Button)findViewById(R.id.SendChat);
        containerMessages = (LinearLayout)findViewById(R.id.ChatLayout);
        scrollView = (ScrollView)findViewById(R.id.ScrollChat);
        ajout = (ImageButton) findViewById(R.id.TchatAjout);
        visibility = false;
        constraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintChat);


        Firebase.setAndroidContext(this);
        salon = new Firebase("https://my-project-1513382873714.firebaseio.com/messages/" +groupe.getGroupeId());

        envoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = message.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<>();
                    map.put("message", messageText);
                    map.put("user", username);
                    salon.push().setValue(map);
                    message.setText("");
                }
            }
        });


        salon.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(username)){
                    addMessageBox("Vous",message, 1);
                }
                else{
                    addMessageBox(userName ,message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public View.OnClickListener onClickAjout(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!visibility) {
                    visibility=true;
                    listAmis = new ScrollView(TchatActivity.this);
                    listAmis.setBackgroundColor(getResources().getColor(R.color.blanc));
                    listAmis.setVisibility(ListView.VISIBLE);
                    listAmis.setBackground(getDrawable(R.drawable.border));
                    listAmis.setPadding(10,10,10,10);
                    LinearLayout listInScrollLayout = new LinearLayout(TchatActivity.this);
                    listInScrollLayout.setOrientation(LinearLayout.VERTICAL);
                    for (AmiDTO ami : amis) {
                        if(!groupe.getMembre().contains(ami.getAmi(username))) {
                            TextView textView = new TextView(TchatActivity.this);
                            textView.setTextSize(20);
                            textView.setText(ami.getAmi(username));
                            textView.setOnClickListener(onClickAddAmis(ami.getAmi(username)));
                            listInScrollLayout.addView(textView);
                        }
                    }
                    listAmis.addView(listInScrollLayout);
                    constraintLayout.addView(listAmis);
                }
                else{
                    visibility = false;
                    listAmis.setVisibility(ListView.INVISIBLE);
                }
            }
        };
    }

    public View.OnClickListener onClickAddAmis (final String ami){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncAddAmiToSalon().execute(ami);
            }
        };
    }

    public void addMessageBox(String user,String message, int type){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;



        TextView textViewMessage = new TextView(this);
        RelativeLayout containerMessage = new RelativeLayout(this);
        TextView textViewSender = new TextView(this);
        LinearLayout vertical = new LinearLayout(this);
        vertical.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout layoutMessage=new RelativeLayout(this);
        textViewMessage.setText(message);
        textViewMessage.setTextColor(getResources().getColor(R.color.blanc));
        textViewMessage.setTextSize(20);


        if(user.equals("Vous")){
            layoutParams.gravity = Gravity.RIGHT;
            containerMessage.setBackground(getDrawable(R.drawable.message));
        }
        else {
            containerMessage.setBackground(getDrawable(R.drawable.message_autre));
        }

        containerMessage.setPadding(30,30,30,30);
        containerMessage.addView(textViewMessage);
        textViewSender.setText(user);
        textViewSender.setTextSize(15);
        vertical.addView(textViewSender);
        vertical.addView(containerMessage);



        layoutMessage.setPadding(10,10,10,10);
        layoutMessage.addView(vertical);

        layoutMessage.setLayoutParams(layoutParams);

        containerMessages.addView(layoutMessage);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,AllChatRoomActivity.class));
    }

    private class AsyncGetAllOtherFriends extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                UserDAO userDAO = new UserDAO(preferences.getString("token",null));
                UserDTO userDTO = userDAO.getByUsername(username);
                amis = userDTO.getAmis();
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(amis!=null) {
                ajout.setOnClickListener(onClickAjout());
            }
        }
    }

    private class AsyncAddAmiToSalon extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... amis) {
            try {
                GroupeDAO groupeDAO = new GroupeDAO(preferences.getString("token",null));
                GroupeDTO groupeDTO = new GroupeDTO();
                groupeDTO.setGroupeId(groupe.getGroupeId());
                groupeDTO.getMembre().add(amis[0]);
                groupeDAO.post(groupeDTO);
            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            visibility = false;
            listAmis.setVisibility(ListView.INVISIBLE);
        }
    }
}