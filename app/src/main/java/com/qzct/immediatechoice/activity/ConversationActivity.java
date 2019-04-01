package com.qzct.immediatechoice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;

/**
 * Created by qin on 2017/3/25.
 */

public class ConversationActivity extends FragmentActivity {

    private TextView tv_name;
    private TextView tv_more;
    private ImageView iv_top_back;
    private int OPEN_USERINFO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initView();
        initData();
    }

    private void initView() {
        iv_top_back = (ImageView) findViewById(R.id.top_back);
        tv_name = (TextView) findViewById(R.id.name);
        tv_more = (TextView) findViewById(R.id.tv_more);

    }

    private void initData() {
        //获取Id
        final String targetId = getIntent().getData().getQueryParameter("targetId");
        String name = getIntent().getData().getQueryParameter("title");
        iv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ConversationActivity.this, "打开用户介绍", Toast.LENGTH_SHORT).show();

                User user = null;
                for (User temp : MyApplication.userList) {
                    if (temp.getUser_id() == Integer.parseInt(targetId)) {
                        user = temp;
                        break;
                    }
                }
                if (user == null) {
                    //Toast.makeText(ConversationActivity.this, "加该用户为好友后才能查看", Toast.LENGTH_SHORT).show();

                    Service.getInstance().getOtherInfo(new MyCallback.OtherInfoCallBack() {
                        @Override
                        public String getUserId() {
                            return targetId;
                        }

                        @Override
                        public void onSuccess(User other) {
                            User user = other;
                            Intent intent = new Intent(ConversationActivity.this, UserInfoActivity.class);
                            intent.putExtra("user", user);
                            intent.putExtra("user_type", User.USER_QUERY);
                            startActivityForResult(intent, OPEN_USERINFO);
//                            finish();
                        }

                        @Override
                        public void onError(Throwable ex) {
                            Toast.makeText(getApplicationContext(), "连接服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                Intent intent = new Intent(ConversationActivity.this, UserInfoActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("user_type", User.USER_FRIEND);
//                    MyApplication.queryfriend = user;
//                    sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
                startActivityForResult(intent, OPEN_USERINFO);
//                finish();
            }
        });
        if (!TextUtils.isEmpty(name)) {
            tv_name.setText(name);
        } else {
            //id
            //TODO  拿到Id 去请求服务端

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_USERINFO) {
            setResult(RESULT_OK);
            finish();
        }

    }
}
