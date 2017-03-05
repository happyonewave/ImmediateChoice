package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.qzct.immediatechoice.LoginActivity;
import com.qzct.immediatechoice.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.RegisterActivity;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-02-26.
 */
public class RegisterFinallyFragment extends baseFragment {

    String phone_number;
    String sex;
    Intent intent;
    CircleImageView user_portrait;
    String portrait_path;
    int IMAGE_PORTRAIT_UPLOAD = 0;

    public RegisterFinallyFragment() {
        super();
    }

    public RegisterFinallyFragment(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        registerActivity.setTitleColor(1);
        final View v = View.inflate(context, R.layout.fill_password, null);
        Button bt_register_finish = (Button) v.findViewById(R.id.bt_register_finish);
        user_portrait = (CircleImageView) v.findViewById(R.id.iv_upload_portrait);
        user_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建Intent
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//              intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, IMAGE_PORTRAIT_UPLOAD);

            }
        });
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
                User user = new User(name, password, phone_number, portrait_path, sex);
                RegisterTask registerTask = new RegisterTask(user);
                registerTask.execute();

                Toast.makeText(context, "下一步已点击", Toast.LENGTH_LONG).show();
            }
        });


        return v;


    }


    public String getPathFromActivityResult(Intent data) {
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        if (data != null) {
//            ContentResolver resolver = getContentResolver();
            Uri originalUri = data.getData();        //获得图片的uri
            String path = utils.getImageAbsolutePath(context, originalUri);
            return path;
        } else {
            return "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            user_portrait.setImageURI(data.getData());
            portrait_path = getPathFromActivityResult(data);
        }


    }


    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            switch (buttonView.getId()) {
                case R.id.radio_man:
                    if (isChecked) {
                        sex = "男";
                    }

                    break;

                case R.id.radio_woman:
                    if (isChecked) {
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
        String portrait_path;

        public RegisterTask(User user) {
            this.name = user.getUsername();
            this.password = user.getPassword();
            this.phone_number = user.getPhone_number();
            this.sex = user.getSex();
            this.portrait_path = user.getPortrait_path();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            String url = MyApplication.url_register;
            HttpPost httpPost = new HttpPost(url);
            try {
//                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//                BasicNameValuePair pair1 = new BasicNameValuePair("name", name);
//                BasicNameValuePair pair2 = new BasicNameValuePair("password", password);
//                BasicNameValuePair pair3 = new BasicNameValuePair("phone_number", phone_number);
//                BasicNameValuePair pair4 = new BasicNameValuePair("sex", sex);
//                parameters.add(pair1);
//                parameters.add(pair2);
//                parameters.add(pair3);
//                parameters.add(pair4);
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");


                Charset charset = Charset.forName("utf-8");
                MultipartEntity entity = new MultipartEntity();
                FileBody portrait = new FileBody(new File(portrait_path));
                entity.addPart("name", new StringBody(name, charset));
                entity.addPart("password", new StringBody(password, charset));
                entity.addPart("phone_number", new StringBody(phone_number, charset));
                entity.addPart("sex", new StringBody(sex, charset));
                entity.addPart("portrait", portrait);
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
