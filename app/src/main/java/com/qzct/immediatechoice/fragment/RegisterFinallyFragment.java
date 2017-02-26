package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.LoginActivity;
import com.qzct.immediatechoice.RegisterActivity;
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

/**
 * Created by Administrator on 2017-02-26.
 */
public class RegisterFinallyFragment extends baseFragment  {

    String phone_number;
    String sex;
    public RegisterFinallyFragment(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public View initview() {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        registerActivity.setTitleColor(1);
        final View v = View.inflate(context, R.layout.fill_password, null);
        Button bt_register_finish = (Button) v.findViewById(R.id.bt_register_finish);
        RadioButton radio_man = (RadioButton) v.findViewById(R.id.radio_man);
        RadioButton radio_woman = (RadioButton) v.findViewById(R.id.radio_woman);
        radio_man.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        radio_woman.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        bt_register_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {

                EditText et_name = (EditText) v.findViewById(R.id.et_name);
                String name = et_name.getText().toString();
                EditText et_password = (EditText) v.findViewById(R.id.et_password);
                String password = et_password.getText().toString();
                RegisterTask registerTask = new RegisterTask(name,password,phone_number,sex);
                registerTask.execute();

                Toast.makeText(context, "下一步已点击", Toast.LENGTH_LONG).show();
            }
        });


        return v;


    }
    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            switch (buttonView.getId()){
                case R.id.radio_man:
                    if (isChecked){
                         sex = "男";
                    }

                    break;

                case R.id.radio_woman:
                    if (isChecked){
                         sex = "女";
                    }

                    break;
            }

        }
    }

    class RegisterTask extends AsyncTask<String, String, String> {
        String name;
        String password;
        String phone_number;
        String sex;

        public RegisterTask(String name, String password, String phone_number, String sex) {
            this.name = name;
            this.password = password;
            this.phone_number = phone_number;
            this.sex = sex;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            String url = getString(R.string.url_register);
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                BasicNameValuePair pair1 = new BasicNameValuePair("name", name);
                BasicNameValuePair pair2 = new BasicNameValuePair("password", password);
                BasicNameValuePair pair3 = new BasicNameValuePair("phone_number", phone_number);
                BasicNameValuePair pair4 = new BasicNameValuePair("sex", sex);
                parameters.add(pair1);
                parameters.add(pair2);
                parameters.add(pair3);
                parameters.add(pair4);


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
                    Toast.makeText(context, "注册失败，此账号可能已被使用!", Toast.LENGTH_LONG).show();
                    break;

                case "1":
                    Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    break;

                case "2":
                    Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
                    break;


                default:
                    break;
            }

        }
    }



    @Override
    public void initdata() {

    }
}
