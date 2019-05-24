package com.qzct.immediatechoice.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nanchen.compresshelper.CompressHelper;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.GroupInfo;
import com.qzct.immediatechoice.domain.Topic;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.PathUtils;
import com.qzct.immediatechoice.util.Service;
import com.qzct.immediatechoice.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qin on 2017/4/2.
 */

public class AddTopicActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int IMAGE_TOPIC_UPLOAD = 0;
    private ImageView iv_topic_img;
    private EditText et_topic_title;
    private Button bt_add;
    private String path_topic;
    private File flle_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Utils.getHasTopView(this, R.layout.activity_add_topic, "添加话题"));
        initView();
        initData();

    }


    private void initView() {
        iv_topic_img = (ImageView) findViewById(R.id.iv_topic_img);
        et_topic_title = (EditText) findViewById(R.id.et_topic_title);
        bt_add = (Button) findViewById(R.id.bt_add);

    }

    private void initData() {
        iv_topic_img.setOnClickListener(this);
        bt_add.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //话题图片
            case R.id.iv_topic_img:
                //选择图片
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_TOPIC_UPLOAD);
                break;
            //上传 添加
            case R.id.bt_add:
                final String topic_title = et_topic_title.getText().toString();
                if (!"".equals(topic_title) && flle_topic != null && flle_topic.exists()) {
                    Service.getInstance().addTopic(new MyCallback.AddTopicCallback() {
                        @Override
                        public Topic getTopic() {

                            return new Topic(topic_title,Utils.getNetUrlFormLocalPath(path_topic,"image"));
                        }

                        @Override
                        public File getImgFile() {
                            return flle_topic;
                        }

                        @Override
                        public void onTopicInvalid() {
                            Toast.makeText(AddTopicActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onImgFileInvalid() {
                            Toast.makeText(AddTopicActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int code) {
                            switch (code) {
                                case 0:
                                    Toast.makeText(AddTopicActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(AddTopicActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(AddTopicActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddTopicActivity.this, "话题图片未选择或选择错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 从其他Activity返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                //话题图片
                case IMAGE_TOPIC_UPLOAD:
                    DisposeResultImage(IMAGE_TOPIC_UPLOAD, data);
                    break;

                default:
                    break;

            }
        }
    }

    /**
     * 处理返回的图片
     *
     * @param what
     * @param data
     */
    private boolean DisposeResultImage(int what, Intent data) {
        switch (what) {
            case IMAGE_TOPIC_UPLOAD:
                path_topic = PathUtils.getPathFromActivityResult(this, data);
                if (path_topic != null) {
                    flle_topic = CompressHelper.getDefault(this).compressToFile(new File(path_topic));
                    iv_topic_img.setPadding(0, 0, 0, 0);
                    iv_topic_img.setBackgroundColor(Color.TRANSPARENT);
                    Bitmap bitmap = BitmapFactory.decodeFile(path_topic);
                    iv_topic_img.setImageBitmap(bitmap);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
