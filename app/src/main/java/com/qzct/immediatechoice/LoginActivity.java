package com.qzct.immediatechoice;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

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
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void login(View v) {
        System.out.println("activity_login");
        EditText et_username = (EditText) findViewById(R.id.et_username);
        EditText et_password = (EditText) findViewById(R.id.et_password);

        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        user = new User(username, password);
    if (isNetworkAvailable(getApplication())){
        LoginTask loginTask = new LoginTask(MyApplication.url_login, user);
        loginTask.execute();
    }else{
        Toast.makeText(this, "你确定网络可以用吗？", Toast.LENGTH_SHORT).show();
    }

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
        String phone_number;
        String sex;
        String portrait_path;

        public LoginTask(String url, User user) {
            this.url = url;
            this.username = user.getUsername();
            this.password = user.getPassword();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
//            String url = getString(R.string.url_login);
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
                e.printStackTrace();
                Log.e("Exception","网络连接失败");
                return "2";
            }
        }

        @Override
        protected void onPostExecute(String text) {
            Log.e("text", text);

            switch (text) {
                case "0":
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;

//                case "1":
//
//                   MyApplication myApplication = (MyApplication) getApplication();
//                    myApplication.setUser(user);
//                    Intent intent = new Intent();
//                    intent.setClass(getBaseContext(), MainActivity.class);
//                    startActivity(intent);
//                    break;

                case "2":
                    Toast.makeText(LoginActivity.this, "连接网站失败", Toast.LENGTH_SHORT).show();
                    break;

                default:

                    try {
                        JSONObject json = new JSONObject(text);
                         phone_number = json.getString("phone_number");
                         sex = json.getString("sex");
                         portrait_path = json.getString("portrait_path");
//                        user_all.setPhone_number(phone_number);
//                        user_all.setSex(sex);
//                        user_all.setPortrait_path(portrait_path);
                        Log.i("user", user.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    User user_all = new User(username,password,phone_number,portrait_path,sex);
                    MyApplication.user = user_all;
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();


                    break;
            }

        }
    }
}

