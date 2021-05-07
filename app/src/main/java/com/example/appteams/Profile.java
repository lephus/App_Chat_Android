package com.example.appteams;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class Profile extends AppCompatActivity {
    String IDUSER = "";
    TextView tvprofilename;
    TextView tvprofileaddress;
    TextView tvprofilegender;
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
        setContentView(R.layout.activity_profile);
        // thuc hien anh xa
        tvprofilename = (TextView) findViewById(R.id.profilename);
        tvprofileaddress = (TextView) findViewById(R.id.address);
        tvprofilegender = (TextView) findViewById(R.id.gender);
        IDUSER = getIntent().getStringExtra("iduser");
        mSocket.connect();
        mSocket.emit("Client-send-viewProfile", IDUSER);
        mSocket.on("server-send-viewProfile", onRetrieveData);
    }
    private Emitter.Listener onRetrieveData = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        JSONArray ob1 = object.getJSONArray("profile");
                        JSONObject res = ob1.getJSONObject(0);
                        tvprofilename.setText(res.getString("fullname"));
                        tvprofileaddress.setText(res.getString("address"));
                        tvprofilegender.setText(res.getString("gender"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}