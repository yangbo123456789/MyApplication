package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private TextView t1,t2,t3;
    private EditText e1,e2;
    private Button b1;
    private String  ResultObj, AccessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        b1=findViewById(R.id.b1);
    }

    public  void wq (View v) {
        Thread t = new Thread(r);
        t.start();
    }


    Runnable r=new Runnable() {
        @Override
        public void run() {
            String h1 = e1.getText().toString();
            String h2 = e2.getText().toString();

            try {
                String SERVER_URL = "http://api.nlecloud.com/Users/Login";
                URL url = new URL(SERVER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStream outputStream = connection.getOutputStream();
                UserBean bean = new UserBean();
                bean.setAccount(h1);
                bean.setPassword(h2);
                bean.setIsRememberMe(true);
                Gson gson = new Gson();
                String beanjson = gson.toJson(bean);//生成json字符串
                outputStream.write(beanjson.getBytes());
                outputStream.flush();
                outputStream.close();
                Message msg=new Message();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();
                    JSONObject jsonObject=new JSONObject(result);
                    ResultObj = jsonObject.getString("ResultObj");
                    JSONObject jsonObject1 = new JSONObject(ResultObj);
                    AccessToken = jsonObject1.getString("AccessToken");
                    msg.what = 1;
                    msg.obj=AccessToken ;
                    handler.sendMessage(msg);
                } else {
                    msg.what=2;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    t3.setText(msg.obj.toString());
                    Intent intent=new Intent(MainActivity.this,jiemian.class);
                    intent.putExtra("token",AccessToken);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

}
