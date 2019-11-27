package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2019/11/20.
 */

public class jiemian extends Activity {
private TextView t11,t22;
private String token;
private JSONObject jsonObject,jsonObject11;
private String ResultObj1,Sensors,Status;
String [] Name={"","","",""},ApiTag={"","","",""},unit={"",""},value={"",""};
private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiemian);
        t11=findViewById(R.id.t11);
        t22=findViewById(R.id.t22);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        Thread thread=new Thread(runnable);
        thread.start();
        if(Name [1]!=null & ApiTag [1]!=null){
            Thread thread1=new Thread(runnable1);
            thread1.start();
        }
    }

   Handler handler=new Handler() {

       @Override
       public void handleMessage(Message msg) {
         switch (msg.what){
             case 1:
                 t11.setText(value[1]+unit[1]);
                 break;
             case 3:
                 Toast.makeText(jiemian.this, "操作成功！", Toast.LENGTH_SHORT).show();
                 break;
             case 4:
                 Toast.makeText(jiemian.this, "操作失败，请重试！！！", Toast.LENGTH_SHORT).show();
                 break;
         }
       }
   };

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://api.nlecloud.com/Devices/60361");
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Connection","Keep-Alive");
                connection.setRequestProperty("Charset","UTF-8");
                connection.setRequestProperty("AccessToken",token);
                //Message msg=new Message();
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
                {
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    String line;
                    while((line=reader.readLine())!=null) {
                        jsonObject=new JSONObject(line);
                        ResultObj1 = jsonObject.getString("ResultObj");
                        JSONObject jsonObject1 = new JSONObject(ResultObj1);
                        Sensors = jsonObject1.getString("Sensors");
                        jsonArray=new JSONArray(Sensors);
                        for (int i=0;i<3;i++)
                        {
                            jsonObject1=(JSONObject)jsonArray.get(i) ;
                            Name[i]=jsonObject1.getString("Name");
                            ApiTag[i]=jsonObject1.getString("ApiTag");
                        }
                    }
                    //msg.what=1;
                    //msg.obj=jsonObject;
                    //handler.sendMessage(msg);
                } else {
                    //msg.what=2;
                    //handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

         //湿度读取
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(" http://api.nlecloud.com/devices/60361/Sensors/"+ApiTag[1]+"");
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Connection","Keep-Alive");
                connection.setRequestProperty("Charset","UTF-8");
                connection.setRequestProperty("AccessToken",token);
                Message msg=new Message();
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
                {
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    String line;
                    while((line=reader.readLine())!=null) {
                        jsonObject=new JSONObject(line);
                        ResultObj1 = jsonObject.getString("ResultObj");
                        JSONObject jsonObject1 = new JSONObject(ResultObj1);
                        unit[1]=jsonObject1.getString("Unit");
                        value[1]=jsonObject1.getString("Value");
                    }
                    msg.what=1;
                    //msg.obj=jsonObject;
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



    public  void click1(View v){
        Thread thread=new Thread(runnable2);
        thread.start();
    }

    Runnable runnable2=new Runnable() {
        @Override
        public void run() {
            try {
                //////////风扇  开
                String SERVER_URL = "http://api.nlecloud.com/Cmds?deviceId=60361&apiTag="+ApiTag[3]+" HTTP/1.1";
                URL url = new URL(SERVER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("AccessToken",token);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(1);
                Message msg=new Message();
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();
                    jsonObject11=new JSONObject(result);
                    Status = jsonObject.getString("Status");
                    if (Status!="1"){
                        msg.what=3;
                        msg.obj=jsonObject;
                        handler.sendMessage(msg);
                    }else
                    {
                        msg.what=4;
                        msg.obj=jsonObject;
                        handler.sendMessage(msg);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


}


