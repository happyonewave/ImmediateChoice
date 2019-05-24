package com.qzct.immediatechoice.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.LoginActivity;
import com.qzct.immediatechoice.activity.RegisterActivity;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.GlideCircleTransform;
import com.qzct.immediatechoice.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;


/**
 * Created by Administrator on 2017-02-26.
 */
@SuppressLint("ValidFragment")
public class RegisterFinallyFragment extends BaseFragment {

    String phone_number;
    String sex = "男";
    Intent intent;
    ImageView user_portrait;
    String portrait_path;
    int user_type;
    private View v;
    private AppCompatSpinner spinner;
    private Button bt_register_finish;
    private EditText et_user_id;

    @SuppressLint("ValidFragment")
    public RegisterFinallyFragment() {
    }

    public RegisterFinallyFragment(String phone_number, int user_type) {
        this.phone_number = phone_number;
        this.user_type = user_type;
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        registerActivity.setTitleColor(1);
        v = View.inflate(context, R.layout.fill_password, null);
        bt_register_finish = (Button) v.findViewById(R.id.bt_register_finish);
        user_portrait = (ImageView) v.findViewById(R.id.iv_upload_portrait);
        spinner = (AppCompatSpinner) v.findViewById(R.id.spinner);
        et_user_id = (EditText) v.findViewById(R.id.et_user_id);
        return v;


    }

    @Override
    public void initData() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sex = "男";
                } else {
                    sex = "女";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        user_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建Intent
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, IMAGE_PORTRAIT_UPLOAD);

            }
        });
//        RadioButton radio_man = (RadioButton) v.findViewById(R.id.radio_man);
//        RadioButton radio_woman = (RadioButton) v.findViewById(R.id.radio_woman);
//        radio_man.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
//        radio_woman.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        bt_register_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {
                if (portrait_path == null) {
                    Toast.makeText(context, "请上传头像", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText et_name = (EditText) v.findViewById(R.id.et_name);
                String name = et_name.getText().toString();
                EditText et_password = (EditText) v.findViewById(R.id.et_password);
                String password = et_password.getText().toString();
                User user = new User(name, user_type, password, phone_number, portrait_path, sex);
                RegisterTask registerTask = new RegisterTask(user);
                registerTask.execute();

                Toast.makeText(context, "下一步已点击", Toast.LENGTH_LONG).show();
            }
        });

    }

    public String getPathFromActivityResult(Intent data) {
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        if (data != null) {
            Uri originalUri = data.getData();        //获得图片的uri
            String path = Utils.getImageAbsolutePath(context, originalUri);
            return path;
        } else {
            return "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
//            user_portrait.setImageURI(data.getData());
            Glide.with(context).load(data.getData()).bitmapTransform(new GlideCircleTransform(context)).into(user_portrait);
            portrait_path = getPathFromActivityResult(data);
        }


    }


//    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//            switch (buttonView.getId()) {
//                case R.id.radio_man:
//                    if (isChecked) {
//                        sex = "男";
//                    }
//
//                    break;
//
//                case R.id.radio_woman:
//                    if (isChecked) {
//                        sex = "女";
//                    }
//
//                    break;
//            }
//
//        }
//    }

    public class RegisterTask extends AsyncTask<String, String, String> {
        int user_type;
        String name;
        String password;
        String phone_number;
        String sex;
        String portrait_path;

        public RegisterTask(User user) {
            this.name = user.getUsername();
            this.user_type = user.getUser_type();
            this.password = user.getPassword();
            this.phone_number = user.getPhone_number();
            this.sex = user.getSex();
            this.portrait_path = user.getPortrait_path();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            String url = Config.url_register;
            HttpPost httpPost = new HttpPost(url);
            try {
                Charset charset = Charset.forName("utf-8");
                MultipartEntity entity = new MultipartEntity();
                FileBody portrait = new FileBody(new File(portrait_path));
                entity.addPart("name", new StringBody(name, charset));
                entity.addPart("user_type", new StringBody(user_type + "", charset));
                entity.addPart("password", new StringBody(password, charset));
                entity.addPart("phone_number", new StringBody(phone_number, charset));
                entity.addPart("sex", new StringBody(sex, charset));
                entity.addPart("portrait", portrait);
                entity.addPart("portrait_url", new StringBody(Config.server_img_url + Utils.getFileName(portrait_path), charset));
//                entity.addPart("portrait_url", new StringBody(Utils.getNetUrlFormLocalPath(portrait_path, "image"), charset));
                httpPost.setEntity(entity);
                HttpResponse hr = hc.execute(httpPost);
                if (hr.getStatusLine().getStatusCode() == 200) {
                    InputStream is = hr.getEntity().getContent();
                    return Utils.getTextFromStream(is);
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
            if (text != null) {
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
    }


}
