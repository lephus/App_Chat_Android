package com.example.appteams;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class Room extends AppCompatActivity {
    String IDUSER= "";
    String ListU  = "";
    ListView listView;
    ImageButton button;
    ListView listThanhVien;
    EditText edtaddThanhVien;

    ArrayList<String> arrayListRoomUser;
    ArrayAdapter arrayAdapterRoomUser;

    ArrayList<String> arrayListUser;
    ArrayAdapter arrayAdapterUser;
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
        setContentView(R.layout.activity_room);
        listView =(ListView) findViewById(R.id.listRoom);
        button =(ImageButton) findViewById(R.id.btaddroom);
        listThanhVien = (ListView)findViewById(R.id.thanhvien);
        edtaddThanhVien = (EditText)findViewById(R.id.edtaddthanhvien);
        IDUSER = getIntent().getStringExtra("iduserchat");
        mSocket.connect();
        mSocket.emit("Client-send-viewRooom", IDUSER);
        mSocket.on("server-send-viewRoom", viewroom);
        mSocket.emit("Client-send-thanhvien", IDUSER);
        mSocket.on("server-send-thanhvien", viewthanhvien);
        arrayListRoomUser = new ArrayList<String>();
        arrayAdapterRoomUser = new ArrayAdapter(Room.this, android.R.layout.simple_list_item_1, arrayListRoomUser);
        listView.setAdapter(arrayAdapterRoomUser);

        arrayListUser = new ArrayList<>();
        arrayAdapterUser = new ArrayAdapter(Room.this, android.R.layout.simple_list_item_1, arrayListUser);
        listThanhVien.setAdapter(arrayAdapterUser);
        listThanhVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = arrayListUser.get(position);
                String[] tmpp = data.split("-");
                ListU+="-"+tmpp[1];
                edtaddThanhVien.setText(ListU);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtaddThanhVien.getText().length()>0) {
                    mSocket.emit("Client-send-addromm", IDUSER+edtaddThanhVien.getText()+"#"+IDUSER);
                    mSocket.emit("Client-send-viewRooom", IDUSER);
                    mSocket.on("server-send-viewRoom", viewroom);
                }
                 edtaddThanhVien.setText("");
                edtaddThanhVien.setHint("nhập/chọn id người nhận, VD: 1-2-3");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dataClick = arrayListRoomUser.get(position)+"#"+IDUSER;
                    Intent nextmess = new Intent(Room.this, Messenger.class);
                    nextmess.putExtra("data",dataClick);
                    startActivity(nextmess);
            }
        });
    }

    final  private Emitter.Listener viewthanhvien = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    arrayListUser.clear();
                    try {
                        JSONArray arr = object.getJSONArray("thanhvien");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject res = arr.getJSONObject(i);
                             arrayListUser.add(res.getString("fullname")+"-"+res.getString("id"));
                        }
                        arrayAdapterUser.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    final private Emitter.Listener viewroom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    arrayListRoomUser.clear();
                    try {
                        JSONArray arr = object.getJSONArray("room");
                        for(int i=0; i<arr.length(); i++){
                            arrayListRoomUser.add(arr.getString(i));
                        }
                        arrayAdapterRoomUser.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}