package com.qzct.immediatechoice.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.alexkolpa.fabtoolbar.FabToolbar;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.CommentAdpter;
import com.qzct.immediatechoice.domain.Comment;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.GlideCircleTransform;
import com.qzct.immediatechoice.util.Utils;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by qin on 2017/3/10.
 * 评论
 */

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, Callback.CommonCallback<String> {

    TextView tv_question;
    ImageView image_text_item_img_left;
    ImageView image_text_item_img_right;
    TextView item_username;
    ImageView item_portrait;
    ImageView comment_icon;
//    ImageView share_icon;
    TextView tv_comment_icon;
    TextView tv_share_icon;
    List<Comment> commentList;
    CommentAdpter adpter;
    ListView lv_comment;
    Button bt_add_comment;
    ImageView top_back;
    TextView hint_new_comment;
    TextView et_add_comment_content;
    String comment_content;
    int question_id;
    private JSONArray jsonArray;
    private String PUSH_COMMENT = "0";
    private TextView title;
    private StandardGSYVideoPlayer gsyVideoPlayer_left;
    private StandardGSYVideoPlayer gsyVideoPlayer_right;
    private OrientationUtils orientationUtils;
    private boolean isTransition;
    private Question question;
    private boolean isQuestion = MyApplication.isQuestion;
    private FabToolbar fab_toolbar;
    private View header_layout;
    private String TRANSITION = "TRANSITION";
    private Context context = this;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Utils.getUsableView(this, R.layout.activity_comment, null));
        title = (TextView) findViewById(R.id.comment_top).findViewById(R.id.top_title);
        title.setText("评论");
//        question = MyApplication.question;
        question = (Question) getIntent().getSerializableExtra("question");
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        initView();
        initData();

    }


    private void initView() {
        header_layout = (View) View.inflate(this, R.layout.lv_comment_header, null);
        tv_question = (TextView) header_layout.findViewById(R.id.tv_question);    //拿到相应的View对象
        image_text_item_img_left = (ImageView) header_layout.findViewById(R.id.image_text_item_img_left);
        image_text_item_img_right = (ImageView) header_layout.findViewById(R.id.image_text_item_img_right);
        gsyVideoPlayer_left = (StandardGSYVideoPlayer) header_layout.findViewById(R.id.video_item_left);
        gsyVideoPlayer_right = (StandardGSYVideoPlayer) header_layout.findViewById(R.id.video_item_right);
        item_username = (TextView) header_layout.findViewById(R.id.item_username);
        item_portrait = (ImageView) header_layout.findViewById(R.id.item_portrait);
        comment_icon = (ImageView) header_layout.findViewById(R.id.comment_icon);
//        share_icon = (ImageView) header_layout.findViewById(R.id.share_icon);
        tv_comment_icon = (TextView) header_layout.findViewById(R.id.tv_comment_icon);
        tv_share_icon = (TextView) header_layout.findViewById(R.id.tv_share_icon);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        hint_new_comment = (TextView) findViewById(R.id.hint_new_comment);
        et_add_comment_content = (TextView) findViewById(R.id.et_add_comment_content);
        bt_add_comment = (Button) findViewById(R.id.bt_add_comment);
        fab_toolbar = (FabToolbar) findViewById(R.id.fab_toolbar);
        top_back = (ImageView) findViewById(R.id.top_back);

    }

    private void initData() {
        tv_question.setText(question.getQuestion_content());
        item_username.setText(question.getQuizzer_name());
//        ImageOptions options = new ImageOptions.Builder().setCircular(true)
//                .setFailureDrawableId(R.mipmap.default_portrait).build();
//        x.image().bind(item_portrait, question.getPortrait_url(), options);
        Glide.with(CommentActivity.this).load(question.getPortrait_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).bitmapTransform(new GlideCircleTransform(context)).into(item_portrait);
        item_portrait.setOnClickListener(this);
        tv_comment_icon.setText(question.getComment_count() + "");
        tv_share_icon.setText(question.getShare_count() + "");
        question_id = question.getQuestion_id();
        bt_add_comment.setOnClickListener(this);
        top_back.setOnClickListener(this);
        if (isQuestion) {
//            x.image().bind(image_text_item_img_right, question.getRight_url());
//            x.image().bind(image_text_item_img_left, question.getLeft_url());
            Glide.with(CommentActivity.this).load(question.getRight_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(image_text_item_img_right);
            Glide.with(CommentActivity.this).load(question.getLeft_url()).placeholder(R.mipmap.notdata).error(R.mipmap.notdata).into(image_text_item_img_left);


        } else {
            image_text_item_img_left.setVisibility(View.GONE);
            image_text_item_img_right.setVisibility(View.GONE);
            gsyVideoPlayer_left.setVisibility(View.VISIBLE);
            gsyVideoPlayer_right.setVisibility(View.VISIBLE);
            initVideoPlayer(gsyVideoPlayer_left, question.getLeft_url());
            initVideoPlayer(gsyVideoPlayer_right, question.getRight_url());
        }
        header_layout.setVisibility(View.VISIBLE);
        lv_comment.addHeaderView(header_layout);
        getCommentListfromServer(isQuestion);


    }


    public class SetThumbImageTask extends AsyncTask<String, String, Bitmap> {
        StandardGSYVideoPlayer gsyVideoPlayer;
        String url;

        public SetThumbImageTask(StandardGSYVideoPlayer gsyVideoPlayer, String url) {
            this.gsyVideoPlayer = gsyVideoPlayer;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return Utils.createVideoThumbnail(url);
        }

        //        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Bitmap videoThumbnail) {
            ImageView imageView = new ImageView(context);
//            Bitmap mBitmap = Bitmap.createBitmap(gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight(), Bitmap.Config.ARGB_8888);
            if (videoThumbnail != null) {
//                mBitmap = MyThumbnailUtils.zoomImg(mBitmap, gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight());
//                mBitmap = MyThumbnailUtils.scaleBitmap(mBitmap, gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight());
                //增加封面
                Bitmap mBitmap = Bitmap.createBitmap(videoThumbnail);
                if (gsyVideoPlayer.getWidth() > 0) {
                    mBitmap = Bitmap.createScaledBitmap(videoThumbnail, gsyVideoPlayer.getWidth(), gsyVideoPlayer.getHeight(), true);

                }
                imageView.setImageBitmap(mBitmap);
                gsyVideoPlayer.setThumbImageView(imageView);

            }
        }
    }

    private void initVideoPlayer(final StandardGSYVideoPlayer gsyVideoPlayer, String url) {

        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setImageBitmap(utils.createVideoThumbnail(url));
//        gsyVideoPlayer.setThumbImageView(imageView);
        //增加封面
        SetThumbImageTask setThumbImageTask = new SetThumbImageTask(gsyVideoPlayer, url);
        setThumbImageTask.execute();
        //url
        //设置播放url，第一个url，第二个开始缓存，第三个使用默认缓存路径，第四个设置title
        gsyVideoPlayer.setUp(url, true, null, "");
        //非全屏下，不显示title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //非全屏下不显示返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //打开非全屏下触摸效果
        gsyVideoPlayer.setIsTouchWiget(true);
        //开启自动旋转
        gsyVideoPlayer.setRotateViewAuto(true);
        //全屏首先横屏
        gsyVideoPlayer.setLockLand(false);
        //是否需要全屏动画效果
        gsyVideoPlayer.setShowFullAnimation(false);

        //立即播放
//        gsyVideoPlayer.startPlayLogic();
        //设置全屏按键功能
        orientationUtils = new OrientationUtils(this, gsyVideoPlayer);
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(CommentActivity.this, true, true);

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (!isQuestion) {
            //先返回正常状态
            int val = CommentActivity.this.getWindow().getAttributes().flags;
            // 全屏 66816 - 非全屏 65792
            if (val == 66816) {//全屏
//                gsyVideoPlayer_left.clearFullscreenLayout();
//                gsyVideoPlayer_right.clearFullscreenLayout();
//                Toast.makeText(this, "退出全屏", Toast.LENGTH_SHORT).show();

            }


            if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                gsyVideoPlayer_left.getFullscreenButton().performClick();
//                gsyVideoPlayer_right.getFullscreenButton().performClick();
//                gsyVideoPlayer_left.clearFullscreenLayout();
//                gsyVideoPlayer_right.clearFullscreenLayout();
                return;
            }
            //释放所有
            gsyVideoPlayer_left.setStandardVideoAllCallBack(null);
            gsyVideoPlayer_right.setStandardVideoAllCallBack(null);
            GSYVideoPlayer.releaseAllVideos();
            gsyVideoPlayer_left.clearFullscreenLayout();
            gsyVideoPlayer_right.clearFullscreenLayout();
            if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                super.onBackPressed();
            } else {
//                Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

        } else {
            super.onBackPressed();

        }
    }

    /**
     * 从服务器获取commentList
     */
    private void getCommentListfromServer(boolean isQuestion) {
        RequestParams entity = new RequestParams(Config.url_comment);
        entity.addBodyParameter("msg", "2");
        entity.addBodyParameter("question_id", question_id + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {

                    if (result != "-1") {
                        try {
                            jsonArray = new JSONArray(result);
                            refreshCommentList();
                        } catch (JSONException e) {
                            Log.d(TAG, "不是合法的json");
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 提交点击事件监听
     *
     * @param view
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_comment:
                //判断提交按钮的状态
                comment_content = et_add_comment_content.getText().toString();
                if (!("".equals(comment_content))) {
                    pushComment();
                    et_add_comment_content.setText("");
                    fab_toolbar.hide();
                } else {
                    Toast.makeText(this, "你没有评论哦", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.add_comment_layout:
                fab_toolbar.hide();
                break;

            case R.id.top_back:
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.item_portrait:
//                Intent intent = new Intent(context, UserInfoActivity.class);
//                intent.putExtra()
                break;

            default:

                break;
        }
    }

    /**
     * 刷新CommentList
     */
    private void refreshCommentList() {
        commentList = new ArrayList<Comment>();
        try {
//            jsonArray = new JSONArray();
            //遍历传入的jsonArray
            for (int i = jsonArray.length() - 1; i > -1; i--) {
                JSONObject temp = jsonArray.optJSONObject(i);
                //读取相应内容.
                String comment_content = temp.getString("comment_content");
                String commenter_date = temp.getString("commenter_date");
                String commenter_name = temp.getString("commenter_name");
                String commenter_portrait_url = temp.getString("commenter_portrait_url");
                Comment comment = new Comment(commenter_portrait_url, commenter_date, commenter_name, comment_content);
                commentList.add(comment);
            }
            adpter = new CommentAdpter(this, commentList);
            lv_comment.setAdapter(adpter);
            adpter.onDataChange(commentList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交评论
     */
    private void pushComment() {
        RequestParams entity = new RequestParams(Config.url_comment);
        Date now = new Date();
        String commenter_date = now.toLocaleString();
        entity.addBodyParameter("msg", PUSH_COMMENT);
        entity.addBodyParameter("question_id", question_id + "");
        entity.addBodyParameter("comment_content", comment_content);
        entity.addBodyParameter("commenter_date", commenter_date);
        entity.addBodyParameter("commenter_name", MyApplication.user.getUsername());
        entity.addBodyParameter("commenter_portrait_url", MyApplication.user.getPortrait_path());
        x.http().post(entity, this);
    }


    /**
     * 评论提交回调成功
     *
     * @param result
     */
    @Override
    public void onSuccess(String result) {
        if (result != null) {
            if (result.equals("1")) {
                getCommentListfromServer(isQuestion);
                Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "评论失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}