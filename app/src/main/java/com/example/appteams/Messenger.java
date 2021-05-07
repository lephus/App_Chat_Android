package com.example.appteams;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class Messenger extends AppCompatActivity {
    boolean check = true;
    String IDROOM = "";
    String data = "";
    String ID = "";
    ListView listjoin;
    ListView listchat;
    ImageButton imgbtsend;
    ImageButton imgbtnewfeed;
    EditText edtcontent;
    ArrayList<String> arrayuser;
    ArrayAdapter ArrayAdapteruser;
    ArrayList<String> arraym;
    ArrayAdapter ArrayAdaptermess;
    Map<String, String> MapUser;
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
        setContentView(R.layout.activity_messenger);
        // thuc hien anh xa
        listjoin = (ListView)findViewById(R.id.listjoin);
        listchat = (ListView)findViewById(R.id.listchat);
        imgbtsend = (ImageButton)findViewById(R.id.btsend);
        imgbtnewfeed = (ImageButton)findViewById(R.id.btnewfeed);
        edtcontent  = (EditText)findViewById(R.id.contentchat);
        String getdata =  getIntent().getStringExtra("data");
        String[] splitdata =getdata.split("#");
        data = splitdata[0];
        String[] tmp = data.split("<");
        IDROOM = tmp[0];
        String LISTUSER = tmp[1].substring(0, tmp[1].length()-1);
        ID = splitdata[1]; //chu tai khoan
        MapUser  = new HashMap<>();
        arrayuser = new ArrayList<>();
        ArrayAdapteruser = new ArrayAdapter(Messenger.this, android.R.layout.simple_list_item_1, arrayuser);
        listjoin.setAdapter(ArrayAdapteruser);
        arraym = new ArrayList<>();
        ArrayAdaptermess = new ArrayAdapter(Messenger.this, android.R.layout.simple_list_item_1, arraym);
        listchat.setAdapter(ArrayAdaptermess);
        mSocket.connect();

        mSocket.emit("Client-send-join", LISTUSER);
        mSocket.on("server-send-join", datajoin);

        mSocket.emit("Client-send-chat1",IDROOM);
        mSocket.on("server-send-mess1", mess1);
        mSocket.on("server-send-mess2", mess2);

        imgbtnewfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextmess = new Intent(Messenger.this, newfeed.class);
                nextmess.putExtra("data", ID+"#"+IDROOM);
                startActivity(nextmess);
            }
        });



        imgbtsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtcontent.getText().toString().length() > 0) {
                        // Vao Room
                    mSocket.emit("Client-send-chat2", ID+"-"+edtcontent.getText()+"-"+IDROOM);
                       edtcontent.setText("");
                       edtcontent.setHint("Nhập tin nhắn...");
                }
            }
        });
    }
    final private Emitter.Listener mess2 = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String[] res = object.getString("data").split("-");
                        if(IDROOM.equals(res[2])){
                            arraym.add(MapUser.get(res[0])+": "+res[1]);
                            ArrayAdaptermess.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    final private Emitter.Listener mess1 = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    arraym.clear();
                    try {
                        JSONArray arr = object.getJSONArray("datamess1");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject res = arr.getJSONObject(i);
                            arraym.add(MapUser.get(res.getString("iduser"))+": "+res.getString("content"));
                        }
                        ArrayAdaptermess.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    final  private Emitter.Listener datajoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arrayuser.clear();
                    JSONObject object = (JSONObject) args[0];
                    try {
                        JSONArray arr = object.getJSONArray("data");
                        for(int i =0; i<arr.length(); i++){
                            String[] tmp = arr.getString(i).split("->");
                            MapUser.put(tmp[1], tmp[0]);
                            arrayuser.add(arr.getString(i));
                        }
                        ArrayAdapteruser.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    final private Emitter.Listener update = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String res = object.getString("update");
                        String[] okk = res.split("-");
                        arraym.add( MapUser.get(okk[0]) + ": "+okk[1]);
                        ArrayAdaptermess.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    final private Emitter.Listener onRetrieveVAOROOM = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        JSONArray ob1 = object.getJSONArray("vaoroom");
                        arrayuser.clear();
                        for(int i =0; i< ob1.length();i++){
                            String tmp   = ob1.getString(i);
                            String[] u  =tmp.split("-");
                            MapUser.put(u[0],u[1]);
                            arrayuser.add(u[1]);
                        }
                        ArrayAdapteruser.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    final  private Emitter.Listener onRetrieveMESSVAOROOM = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        JSONArray ob1 = object.getJSONArray("vaoroom2");
                        arraym.clear();
                        for(int i =0; i< ob1.length();i++){
                            JSONObject res = ob1.getJSONObject(i);
                            arraym.add( MapUser.get(res.getString("iduser")) + ": "+res.getString("content"));
                        }
                        ArrayAdaptermess.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}