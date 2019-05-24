package com.qzct.immediatechoice.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.GlideCircleTransform;
import com.qzct.immediatechoice.util.RSAEncrypt;
import com.qzct.immediatechoice.util.Utils;
import com.tuyenmonkey.mkloader.MKLoader;

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
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {


    String sex = "男";
    Intent intent;
    ImageView user_portrait;
    String portrait_path;
    int IMAGE_PORTRAIT_UPLOAD = 0;
    private AppCompatActivity context = this;
    private CheckBox iv_sex;
    private Button bt_register;
    private boolean sexIsVerify = false;
    private TextView tv_login;
    private int user_type = 1;
    private RadioGroup rg_user_type;
    private ImageView iv_back;
    private MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();

        //多步注册
//        fl_register = (FrameLayout) findViewById(R.id.fl_register);
//        ImageView iv_back = (ImageView) findViewById(R.id.iv_register_back);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        RegisterFirstFragment fragment = new RegisterFirstFragment();
//        transaction.add(R.id.fl_register, (Fragment) fragment);
//        transaction.commit();
    }

    public void initView() {
        iv_sex = (CheckBox) findViewById(R.id.iv_sex);
        user_portrait = (ImageView) findViewById(R.id.user_portrait);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_register = (Button) findViewById(R.id.bt_register);
        tv_login = (TextView) findViewById(R.id.tv_login);
        rg_user_type = (RadioGroup) findViewById(R.id.rg_user_type);
        loader = (MKLoader) findViewById(R.id.loader);


    }

    public void initData() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = sex.equals("男") ? "女" : "男";
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        rg_user_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioBtn = (RadioButton) group.findViewById(checkedId);
                String radioBtn_text = radioBtn.getText().toString();
                user_type = radioBtn_text.equals("个人") ? 1 : 0;
            }
        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    sex = "男";
//                } else {
//                    sex = "女";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
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
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {
                if (portrait_path == null) {
                    Toast.makeText(context, "请上传头像", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sexIsVerify) {
                    EditText et_name = (EditText) findViewById(R.id.et_name);
                    String name = et_name.getText().toString();
                    EditText et_password = (EditText) findViewById(R.id.et_password);
                    String password = et_password.getText().toString();
                    User user = new User(name, user_type, password, name, portrait_path, sex);
                    RegisterTask registerTask = new RegisterTask(user);
                    Toast.makeText(context, "请稍等", Toast.LENGTH_SHORT).show();
                    registerTask.execute();

//                    Toast.makeText(context, "性别：" + sex + "用户类型：" + user_type + "用户名：" + name + "密码： " + password, Toast.LENGTH_LONG).show();
//                    Toast.makeText(context, "下一步已点击", Toast.LENGTH_LONG).show();
                } else {
                    sexIsVerify = true;
                    Toast.makeText(context, "请确认性别与用户类型后再次点击", Toast.LENGTH_LONG).show();
                }

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


    class RegisterTask extends AsyncTask<String, String, String> {
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
            loader.setVisibility(View.VISIBLE);
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
                entity.addPart("password", new StringBody(RSAEncrypt.encrypt(password), charset));
                entity.addPart("phone_number", new StringBody(phone_number, charset));
                entity.addPart("sex", new StringBody(sex, charset));
                entity.addPart("portrait", portrait);
                entity.addPart("portrait_url", new StringBody(Config.server_img_url + Utils.getFileName(portrait_path), charset));
//                entity.addPart("portrait_url", new StringBody(utils.getNetUrlFormLocalPath(portrait_path, "image"), charset));
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
            loader.setVisibility(View.GONE);
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


    //多步注册
//    public void setTitleColor(int code) {
//        TextView tv_fill_verification_code;
//        switch (code) {
//            case 0:
//                TextView tv_fill_tel = (TextView) findViewById(R.id.tv_fill_tel);
//                tv_fill_verification_code = (TextView) findViewById(R.id.tv_fill_verification_code);
//                tv_fill_tel.setTextColor(getResources().getColor(R.color.gray));
//                tv_fill_verification_code.setTextColor(getResources().getColor(R.color.apporange));
//                break;
//            case 1:
//                tv_fill_verification_code = (TextView) findViewById(R.id.tv_fill_verification_code);
//                tv_fill_verification_code.setTextColor(getResources().getColor(R.color.gray));
//                TextView tv_set_password = (TextView) findViewById(R.id.tv_set_password);
//                tv_set_password.setTextColor(getResources().getColor(R.color.apporange));
//                break;
//            case 2:
//
//                break;
//
//            default:
//
//                break;
//        }
//    }


}