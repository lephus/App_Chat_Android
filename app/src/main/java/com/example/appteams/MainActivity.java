package com.example.appteams;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private  boolean display = false;
    public String IDUSER = "-1";
    String LEVEL = "";
    ListView listM;

    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;

    int REQUEST_LOGIN = 123;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.17:3000/");
        } catch (URISyntaxException uriSyntaxException) {
            uriSyntaxException.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_LOGIN && resultCode == RESULT_OK && data != null){
            String[] tmp = data.getStringExtra("user").split("-");
            IDUSER =tmp[0];
            LEVEL = tmp[1];
            display = true;
            Toast.makeText(MainActivity.this, IDUSER, Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listM = (ListView)findViewById(R.id.listview);
        if(!display){
            display = true;
            startActivityForResult( new Intent(MainActivity.this, LoginActivity1.class), REQUEST_LOGIN);
        }

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        listM.setAdapter(arrayAdapter);

        mSocket.connect();
        mSocket.emit("Client", IDUSER);
        mSocket.on("all", viewnewfeed);
    }
    final private Emitter.Listener viewnewfeed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    arrayList.clear();
                    try {
                        JSONArray arr = object.getJSONArray("data");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject res = arr.getJSONObject(i);
                            arrayList.add(res.getString("auth")+" Title: "+res.getString("title"));
                            arrayList.add("Content: "+res.getString("content"));
                        }
                        arrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.idhome:
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_LONG).show();
                break;
            case R.id.idchat1:
                Intent nextChat = new Intent(MainActivity.this, Room.class);
                nextChat.putExtra("iduserchat",IDUSER);
                startActivity(nextChat);
                break;
            case R.id.idsetting:
                 if(!LEVEL.equals("0")){
                     Toast.makeText(MainActivity.this, "WELLCOME ADMIN", Toast.LENGTH_LONG).show();
                     Intent nextSetting = new Intent(MainActivity.this, SettingSystem.class);
                    nextSetting.putExtra("data",IDUSER);
                    startActivity(nextSetting);
                }else{
                     Toast.makeText(MainActivity.this, "Chức Năng Chỉ Dành Cho Admin", Toast.LENGTH_LONG).show();
                 }
                break;
            case R.id.idprofile:
                Intent nextProfile = new Intent(MainActivity.this, Profile.class);
                nextProfile.putExtra("iduser",IDUSER);
                startActivity(nextProfile);
                break;
            case R.id.idlogout:
                // create intent to show Main Activity
                startActivityForResult( new Intent(MainActivity.this, LoginActivity1.class), REQUEST_LOGIN);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}