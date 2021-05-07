package com.example.appteams;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class LoginActivity1 extends AppCompatActivity{
    private EditText edtuserName;
    private EditText edtpass;
    private Button btlogin;
    private Button btsup;
    String user = "";
    String pass = "";
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
        setContentView(R.layout.activity_login1);
        edtuserName =(EditText) findViewById(R.id.username);
        edtpass =(EditText) findViewById(R.id.password);
        btlogin =(Button) findViewById(R.id.btlogin);
        btsup =(Button) findViewById(R.id.support);
        // connect server
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("------------- connect -----------");
                mSocket.connect();
                System.out.println(mSocket.connected());
                System.out.println("------------- disconnect -----------");
                mSocket.disconnect();
                System.out.println(mSocket.connected());
                System.out.println("------------- close -----------");
                mSocket.close();
                System.out.println(mSocket.connected());
                mSocket.connect();
               // Toast.makeText(LoginActivity1.this, "connect to server"+mSocket.id(), Toast.LENGTH_SHORT).show();

                user = (String) edtuserName.getText().toString();
                 pass= (String) edtpass.getText().toString();
                if(!user.isEmpty()&& !pass.isEmpty() && !user.equals("")){
                    // check login

                    mSocket.emit("client-login", user+"-"+pass);
                    mSocket.on("server-send-login", onRetrieveLogin);
                }else{
                    Toast.makeText(LoginActivity1.this, "Đăng nhập thất bại!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity1.this);
                b.setTitle("Liên hệ hỗ trợ");
                b.setMessage("Vui lòng Liên hệ Admin để được hỗ trợ.");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog al = b.create();
                al.show();
            }
        });
    }
    private Emitter.Listener onRetrieveLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object =  (JSONObject) args[0];
                    try {
                        String check = object.getString("checklogin");
                        if(!check.equals("-1") ){
                            mSocket.disconnect();
                            Intent intent = new Intent();
                            intent.putExtra("user", check );
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            mSocket.disconnect();
                         }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}