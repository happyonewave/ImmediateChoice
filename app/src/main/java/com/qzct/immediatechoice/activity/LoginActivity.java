package com.qzct.immediatechoice.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
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

import static com.qzct.immediatechoice.R.id.bt_login;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    User user;
    public static LoginActivity loginActivity;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = this;
        //登录
        Button bt_login = (Button) findViewById(R.id.bt_login);
        //注册
        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        bt_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);

    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case bt_login:
                login();
                break;
            //注册
            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                break;
            default:

                break;
        }

    }

    /**
     * 判断网络连接
     *
     * @param context
     * @return
     */
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

    /**
     * 登录
     */
    public void login() {

        EditText et_username = (EditText) findViewById(R.id.et_username);
        EditText et_password = (EditText) findViewById(R.id.et_password);

        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        user = new User(username, password);
        //判断网络连接
        if (isNetworkAvailable(getApplication())) {
            LoginTask loginTask = new LoginTask(Config.url_login, user);
            loginTask.execute();
        } else {
            Toast.makeText(this, "你确定网络可以用吗？", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 异步登录
     */
    class LoginTask extends AsyncTask<String, String, String> {
        String url;
        int user_id;
        int user_type;
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

        //后台处理
        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
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
                //返回码
                if (hr.getStatusLine().getStatusCode() == 200) {
                    InputStream is = hr.getEntity().getContent();
                    String result = utils.getTextFromStream(is);
                    return result;
                } else {
                    return "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "2";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            switch (result) {
                case "0":
                    Toast.makeText(LoginActivity.this, "帐号或密码错误！请重新登录！", Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(LoginActivity.this, "连接网站失败", Toast.LENGTH_SHORT).show();

                    break;
//                0

                default:
                    //登录成功
                    try {
                        JSONObject json = new JSONObject(result);
                        //获取User信息
                        user_id = json.optInt("user_id");
                        user_type = json.optInt("user_type");
                        phone_number = json.optString("phone_number");
                        sex = json.optString("sex");
                        portrait_path = json.optString("portrait_path");
                        token = json.optString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    User user_all = new User(user_id, user_type, username, password, phone_number, portrait_path, sex, token);
                    //存储User到Application
                    MyApplication.user = user_all;
                    MyApplication.logined = true;
                    if (user_type == 0) {
                        MyApplication.isQuestionnaireProvider = true;
                    }
                    //进入主界面
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    break;
            }

        }
    }
}

