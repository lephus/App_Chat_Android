package com.example.appteams;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
public class SettingSystem extends AppCompatActivity {
    ArrayList<String> arrayListUser;
    ArrayAdapter arrayAdapterUser;
    String IDUSER;
    ListView listUser;
    EditText edtUserName;
    EditText edtPass;
    EditText edtFullName;
    CheckBox ckNam;
    CheckBox ckNu;
    CheckBox ckAdmin;
    EditText edtAddress;
    Button btSubmit;
    String gender = "Nam";
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
        setContentView(R.layout.activity_setting_system);
        // thuc hien anh Xa
        listUser = (ListView)findViewById(R.id.listuer);
        edtUserName = (EditText)findViewById(R.id.username);
        edtPass = (EditText)findViewById(R.id.password);
        edtFullName = (EditText)findViewById(R.id.fullname);
        edtAddress = (EditText)findViewById(R.id.address);
        ckNam = (CheckBox)findViewById(R.id.checkNam);
        ckNu = (CheckBox)findViewById(R.id.checkNu);
        ckAdmin = (CheckBox)findViewById(R.id.checkAdmin);
        btSubmit =  (Button)findViewById(R.id.btsubmit);

        IDUSER = getIntent().getStringExtra("data");

        arrayListUser = new ArrayList<>();
        arrayAdapterUser = new ArrayAdapter(SettingSystem.this, android.R.layout.simple_list_item_1, arrayListUser);
        listUser.setAdapter(arrayAdapterUser);
        mSocket.connect();
        mSocket.emit("Client-send-thanhvien", IDUSER);
        mSocket.on("server-send-thanhvien", viewthanhvien);
        mSocket.on("adduser", view);

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingSystem.this);
                builder.setMessage("Lựa chọn thao tác bạn muốn...")
                        .setPositiveButton("Lên_AD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(SettingSystem.this, "Update Thành Công", Toast.LENGTH_SHORT).show();
                                String[] tmp = arrayListUser.get(position).split("#");
                                mSocket.emit("LENAD", tmp[1]);
                            }
                        })
                        .setNegativeButton("Xóa_AD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(SettingSystem.this, "Update Thành Công", Toast.LENGTH_SHORT).show();
                                String[] tmp = arrayListUser.get(position).split("#");
                                mSocket.emit("XOAAD", tmp[1]);
                            }
                        })
                        .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(SettingSystem.this, "DELETE Thành Công", Toast.LENGTH_SHORT).show();
                                String[] tmp = arrayListUser.get(position).split("#");
                                mSocket.emit("DELETEUSER", tmp[1]);
                            }
                        })
                ;
                builder.create();
                builder.show();
            }
        });

        ckNam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ckNam.isChecked()){
                    ckNu.setChecked(false);
                    gender = "Nam";
                }else{
                    ckNu.setChecked(true);
                    gender = "Nữ";
                }
            }
        });
        ckNu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ckNu.isChecked()){
                    ckNam.setChecked(false);
                    gender = "Nữ";
                }else{
                    ckNam.setChecked(true);
                    gender = "Nam";
                }
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = "0";
                if(ckAdmin.isChecked()){
                    level = "1";
                }
                if(edtUserName.getText().length()>0 &&
                        edtAddress.getText().length()>0 &&
                        edtFullName.getText().length()>0 &&
                        edtPass.getText().length()>0
                ){
                    mSocket.emit("Client-send-ADDUSER", edtUserName.getText()+"#"+edtPass.getText()
                    +"#"+edtFullName.getText()+"#"+gender+"#"+edtAddress.getText()+'#'+level
                    );
                }
            }
        });
    }
    final  private Emitter.Listener view = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    arrayListUser.clear();
                    try {
                        JSONArray arr = object.getJSONArray("data");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject res = arr.getJSONObject(i);
                            arrayListUser.add(res.getString("fullname")+"#"+res.getString("id"));
                        }
                        arrayAdapterUser.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
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
                            arrayListUser.add(res.getString("fullname")+"#"+res.getString("id"));
                        }
                        arrayAdapterUser.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}