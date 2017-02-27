package com.qzct.immediatechoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.User;
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

    User user;
//    Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            String returned = (String) msg.obj;
//            Toast.makeText(LoginActivity.this, returned, Toast.LENGTH_SHORT).show();
////            if (returned.equals("登录成功")){
//            Intent intent = new Intent();
//            intent.setClass(getBaseContext(), MainActivity.class);
//            startActivity(intent);
////            }
//        }
//
//        ;
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("oncreate");
        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    public void login(View v) {
        System.out.println("activity_login");
        EditText et_username = (EditText) findViewById(R.id.et_username);
        EditText et_password = (EditText) findViewById(R.id.et_password);

        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        user = new User(username, password);

        LoginTask loginTask = new LoginTask(getString(R.string.url_login), user);
        loginTask.execute();

//        Thread t = new Thread() {
//            public void run() {
//                EditText et_username = (EditText) findViewById(R.id.et_username);
//                EditText et_password = (EditText) findViewById(R.id.et_password);
//
//                String username = et_username.getText().toString();
//                String password = et_password.getText().toString();
//
//                HttpClient hc = new DefaultHttpClient();
//                String url = getString(R.string.url_login);
//                HttpPost httpPost = new HttpPost(url);
//                try {
//                    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//                    BasicNameValuePair pair1 = new BasicNameValuePair("name", username);
//                    BasicNameValuePair pair2 = new BasicNameValuePair("password", password);
//                    parameters.add(pair1);
//                    parameters.add(pair2);
//
//                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
//                    httpPost.setEntity(entity);
//                    HttpResponse hr = hc.execute(httpPost);
//
//                    if (hr.getStatusLine().getStatusCode() == 200) {
//                        InputStream is = hr.getEntity().getContent();
//                        //String text = Utils.getTextFromStream(is);
//                        String text = utils.getTextFromStream(is);
//                        Message msg = handler.obtainMessage();
//                        msg.obj = text;
//                        handler.sendMessage(msg);
//                    } else {
//                        System.out.println("连接网站失败");
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("异常");
//                    e.printStackTrace();
//                }
//            }
//
//            ;
//        };
//        t.start();


    }

    class LoginTask extends AsyncTask<String, String, String> {
        String url;
        String username;
        String password;

        public LoginTask(String url, User user) {
            this.url = url;
            this.username = user.getUsername();
            this.password = user.getPassword();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            String url = getString(R.string.url_login);
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                BasicNameValuePair pair1 = new BasicNameValuePair("name", username);
                BasicNameValuePair pair2 = new BasicNameValuePair("password", password);
                parameters.add(pair1);
                parameters.add(pair2);

                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                httpPost.setEntity(entity);
                HttpResponse hr = hc.execute(httpPost);
                if (hr.getStatusLine().getStatusCode() == 200) {
                    InputStream is = hr.getEntity().getContent();
                    String text = utils.getTextFromStream(is);
                    return text;
                } else {
                    return "2";
                }
            } catch (Exception e) {
                System.out.println("异常");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String text) {
            switch (text) {
                case "0":
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;

                case "1":

                   MyApplication myApplication = (MyApplication) getApplication();
                    myApplication.setUser(user);
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    break;

                case "2":
                    Toast.makeText(LoginActivity.this, "连接网站失败", Toast.LENGTH_SHORT).show();
                    break;


                default:
                    break;
            }

        }
    }
}

