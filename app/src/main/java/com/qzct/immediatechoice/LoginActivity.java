package com.qzct.immediatechoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.util.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            String returned = (String) msg.obj;
            Toast.makeText(LoginActivity.this, returned,Toast.LENGTH_SHORT).show();
//            if (returned.equals("登录成功")){
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),MainActivity.class);
                startActivity(intent);
//            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        System.out.println("oncreate");
    }

    public void login(View v){
        System.out.println("login");
        Thread t = new Thread(){
            public void run() {
                EditText et_username = (EditText) findViewById(R.id.et_username);
                EditText et_password = (EditText) findViewById(R.id.et_password);

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                HttpClient hc =new  DefaultHttpClient();
                String uri = getString(R.string.url_login);
                HttpPost httpPost = new HttpPost(uri);
                try {
                    List<NameValuePair>  parameters = new ArrayList<NameValuePair>();
                    BasicNameValuePair pair1 = new BasicNameValuePair("name", username);
                    BasicNameValuePair pair2 = new BasicNameValuePair("password", password);
                    parameters.add(pair1);
                    parameters.add(pair2);

                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                    httpPost.setEntity(entity);
                    HttpResponse hr = hc.execute(httpPost);

                    if (hr.getStatusLine().getStatusCode() == 200) {
                        InputStream is = hr.getEntity().getContent();
                        //String text = Utils.getTextFromStream(is);
                        String text = utils.getTextFromStream(is);
                        Message msg = handler.obtainMessage();
                        msg.obj = text;
                        handler.sendMessage(msg);
                    }else {
                        System.out.println("连接网站失败");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("异常");
                    e.printStackTrace();
                }
            };
        };
        t.start();


    }

}
