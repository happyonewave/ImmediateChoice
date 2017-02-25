package com.qzct.immediatechoice;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.domain.question;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PushActivity extends Activity implements View.OnClickListener {

    ImageView push_img_left;
    ImageView push_img_right;
    ImageView iv_push_go;
    String image_left_path;
    String image_right_path;
    private static final int IMAGE_LEFT_UPLOAD = 0;
    private static final int IMAGE_RIGHT_UPLOAD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_push_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PushActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        push_img_left = (ImageView) findViewById(R.id.push_img_left);
        push_img_right = (ImageView) findViewById(R.id.push_img_right);
        iv_push_go = (ImageView) findViewById(R.id.iv_push_go);
        push_img_left.setOnClickListener(this);
        push_img_right.setOnClickListener(this);
        iv_push_go.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_LEFT_UPLOAD:

                image_left_path = getPathFromActivityResult(data);

                break;

            case IMAGE_RIGHT_UPLOAD:
                image_right_path = getPathFromActivityResult(data);
                break;


            default:
                break;

        }
    }

    private String getPathFromActivityResult(Intent data) {
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();
        Uri originalUri = data.getData();        //获得图片的uri
        String path = utils.getImageAbsolutePath(this, originalUri);
        Toast.makeText(this, "Uri:" + originalUri + "path:" + path, Toast.LENGTH_LONG).show();
        return path;

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        question vote = new question();
        switch (v.getId()) {
            case R.id.iv_push_go:
                vote.setImage_left(image_left_path);
                vote.setImage_right(image_right_path);

                EditText et_push_question_content = (EditText) findViewById(R.id.push_question_content);
                String question_content = et_push_question_content.getText().toString();
                UploadTask uploadTask = new UploadTask(getString(R.string.url_upload), "USERNAME", question_content, image_left_path, image_right_path);
                uploadTask.execute();


                intent = new Intent(PushActivity.this, MainActivity.class);
                startActivity(intent);

                break;


            case R.id.push_img_left:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, IMAGE_LEFT_UPLOAD);
                break;

            case R.id.push_img_right:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, IMAGE_RIGHT_UPLOAD);

                break;


            default:
                break;

        }

    }


    class UploadTask extends AsyncTask<String, String, String> {
        String url;
        String image_left_path;
        String image_right_path;
        String question_content;
        String username;

        public UploadTask(String url, String username, String question_content, String image_left_path, String image_right_path) {
            this.url = url;
            this.username = username;
            this.question_content = question_content;
            this.image_left_path = image_left_path;
            this.image_right_path = image_right_path;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
//            String url = getString(R.string.url_image_text);
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                BasicNameValuePair pair1 = new BasicNameValuePair("username", username);
                BasicNameValuePair pair2 = new BasicNameValuePair("question_content", question_content);
                BasicNameValuePair pair3 = new BasicNameValuePair("image_left_path", image_left_path);
                BasicNameValuePair pair4 = new BasicNameValuePair("image_right_path", image_right_path);
                parameters.add(pair1);
                parameters.add(pair2);
                parameters.add(pair3);
                parameters.add(pair4);


//                FileBody image_left = new FileBody(new File(image_left_path));
//                FileBody image_right = new FileBody(new File(image_right_path));
//                MultipartEntity reqEntity = new MultipartEntity();
//                reqEntity.addPart("username", new StringBody(username) );
//                reqEntity.addPart("question_content", new StringBody(question_content));
//                reqEntity.addPart("image_left_path", new StringBody(image_left_path));
//                reqEntity.addPart("image_right_path", new StringBody(image_right_path));
//                reqEntity.addPart("image_left", image_left);
//                reqEntity.addPart("image_right", image_left);
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
                    Toast.makeText(PushActivity.this, "发起投票失败", Toast.LENGTH_LONG).show();
                    break;

                case "1":
                    Toast.makeText(PushActivity.this, "发起投票成功", Toast.LENGTH_LONG).show();

                    break;

                case "2":
                    Toast.makeText(PushActivity.this, "连接网站失败", Toast.LENGTH_LONG).show();
                    break;


                default:
                    break;
            }

        }
    }


}