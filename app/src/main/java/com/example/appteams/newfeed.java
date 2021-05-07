package com.example.appteams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class newfeed extends AppCompatActivity {
    EditText edttitle;
    String ID = "";
    String IDROOM = "";
    EditText edtContent;
    Button btSubmit;
    Button btSubmitall;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.17:3000/");
        } catch (URISyntaxException uriSyntaxException) {
            uriSyntaxException.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfeed);
        edttitle = (EditText)findViewById(R.id.grouptitle);
        edtContent=  (EditText)findViewById(R.id.groupcontent);
        btSubmit = (Button)findViewById(R.id.groupSubmit);
        btSubmitall  = (Button)findViewById(R.id.groupSubmitALL) ;
        listView = (ListView)findViewById(R.id.listnewFeed);

        String[] getdata =  getIntent().getStringExtra("data").split("#");
        ID= getdata[0];
        IDROOM = getdata[1];
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(newfeed.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        mSocket.connect();
        mSocket.emit("viewnewfeed", IDROOM);
        mSocket.on("server-viewnewfeed", viewnewfeed);
        mSocket.on("server-view1", view1 );
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtContent.getText().length()>0 && edttitle.getText().length()>0){
                    mSocket.emit("addnewfeed", ID+"#"+edttitle.getText()+"#"+edtContent.getText()+"#"+IDROOM);

                }
            }
        });
        btSubmitall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtContent.getText().length()>0 && edttitle.getText().length()>0){
                    mSocket.emit("addnewfeedALL", ID+"#"+edttitle.getText()+"#"+edtContent.getText()+"#"+"-1");
                }
            }
        });

    }
    final  private  Emitter.Listener view1 =  new Emitter.Listener() {
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
}