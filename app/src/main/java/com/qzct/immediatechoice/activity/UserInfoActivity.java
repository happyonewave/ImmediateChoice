package com.qzct.immediatechoice.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.GlideCircleTransform;
import com.qzct.immediatechoice.util.MyCallback;
import com.qzct.immediatechoice.util.Service;
import com.qzct.immediatechoice.util.utils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 * Created by tsh2 on 2017/4/16.
 */
public class UserInfoActivity extends Activity {
    private User user;
    private TextView tv_user_name;
    private TextView tv_user_sex;
    private TextView tv_user_phone_num;
    private ImageView iv_user_portrait;
    private int USER_TYPE;
    private Button btn_add_friend;
    private RelativeLayout rl_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_userinfo, "个人信息"));
        user = (User) getIntent().getSerializableExtra("user");
        USER_TYPE = getIntent().getIntExtra("user_type", 0);
        initView();
        initData();
    }

    private void initView() {
        iv_user_portrait = (ImageView) findViewById(R.id.user_portrait);
        tv_user_name = (TextView) findViewById(R.id.user_name);
        tv_user_sex = (TextView) findViewById(R.id.user_sex);
        tv_user_phone_num = (TextView) findViewById(R.id.user_phone_num);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
        if (USER_TYPE != User.USER_ME) {
            btn_add_friend.setVisibility(View.VISIBLE);
            rl_layout.setVisibility(View.GONE);
        }
    }

    private void initData() {
//        final Drawable top = null;
//        SimpleTarget simpleTarget = new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                tv_user_portrait.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(bitmap), null, null);
//            }
//        };
//        Glide.with(this).load(user.getPortrait_path()).asBitmap().into(simpleTarget);

//        tv_user_portrait.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        Glide.with(this).load(user.getPortrait_path()).bitmapTransform(new GlideCircleTransform(this)).into(iv_user_portrait);

        tv_user_name.setText(user.getUsername());
        tv_user_sex.setText(user.getSex());
        tv_user_phone_num.setText(user.getPhone_number());
        if (USER_TYPE == User.USER_FRIEND) {
            btn_add_friend.setText("删除好友");
            btn_add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    deleteFriend(user.getUser_id());
                    Service.getInstance().updateFriend(user.getUser_id(), new MyCallback.UpdateFriendCallback() {
                        @Override
                        public String getUpdateType() {
                            return "delete";
                        }

                        @Override
                        public void onSuccess(String msg) {
                            Log.d("qin", "result: " + msg);
                            Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onError(Throwable ex) {
                            Toast.makeText(UserInfoActivity.this, "请求删除好友失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });

//                }
                }
            });
        } else if (USER_TYPE == User.USER_QUERY) {
            btn_add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    updateFriend(user.getUser_id());
                    Service.getInstance().updateFriend(user.getUser_id(), new MyCallback.UpdateFriendCallback() {
                        @Override
                        public String getUpdateType() {
                            return null;
                        }

                        @Override
                        public void onSuccess(String msg) {
                            Log.d("qin", "result: " + msg);
                            Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }

                        @Override
                        public void onError(Throwable ex) {
                            Toast.makeText(UserInfoActivity.this, "请求加好友失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

//    private void updateFriend(int f_id, String update_type) {
//        RequestParams entity = new RequestParams(Config.url_friend);
//        entity.addBodyParameter("user_id", MyApplication.user.getUser_id() + "");
//        entity.addBodyParameter("f_id", f_id + "");
//        if ("delete".equals(update_type)) {
//            entity.addBodyParameter("update_type", update_type);
//        }
//        x.http().post(entity, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d("qin", "result: " + result);
//                Toast.makeText(UserInfoActivity.this, result, Toast.LENGTH_SHORT).show();
//                setResult(RESULT_OK, new Intent());
//                finish();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(UserInfoActivity.this, "请求加好友失败，请重试", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
//    }

}
