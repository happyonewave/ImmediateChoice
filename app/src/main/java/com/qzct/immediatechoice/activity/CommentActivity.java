package com.qzct.immediatechoice.activity;

import android.content.pm.ActivityInfo;
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

import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.CommentAdpter;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.domain.Comment;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.qzct.immediatechoice.test.TestActivity.TRANSITION;


/**
 * Created by qin on 2017/3/10.
 * 评论
 */

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, Callback.CommonCallback<String> {

    TextView tv_question;
    SmartImageView image_text_item_img_left;
    SmartImageView image_text_item_img_right;
    TextView item_username;
    ImageView item_portrait;
    Button comment_icon;
    Button share_icon;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getUsableView(this, R.layout.activity_comment));
        title = (TextView) findViewById(R.id.comment_top).findViewById(R.id.top_title);
        title.setText("评价");
        question = MyApplication.question;
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        initView();
        initData();

    }


    private void initView() {
        tv_question = (TextView) findViewById(R.id.tv_question);    //拿到相应的View对象
        image_text_item_img_left = (SmartImageView) findViewById(R.id.image_text_item_img_left);
        image_text_item_img_right = (SmartImageView) findViewById(R.id.image_text_item_img_right);
        gsyVideoPlayer_left = (StandardGSYVideoPlayer) findViewById(R.id.video_item_left);
        gsyVideoPlayer_right = (StandardGSYVideoPlayer) findViewById(R.id.video_item_right);
        item_username = (TextView) findViewById(R.id.item_username);
        item_portrait = (ImageView) findViewById(R.id.item_portrait);
        comment_icon = (Button) findViewById(R.id.comment_icon);
        share_icon = (Button) findViewById(R.id.share_icon);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        hint_new_comment = (TextView) findViewById(R.id.hint_new_comment);
        et_add_comment_content = (TextView) findViewById(R.id.et_add_comment_content);
        bt_add_comment = (Button) findViewById(R.id.bt_add_comment);
        top_back = (ImageView) findViewById(R.id.top_back);
    }

    private void initData() {
        tv_question.setText(question.getQuestion_content());
        item_username.setText(question.getQuizzer_name());
//            item_portrait.setImageDrawable(question.getQuizzer_portrait());
        ImageOptions options = new ImageOptions.Builder().setCircular(true)
                .setFailureDrawableId(R.mipmap.default_portrait).build();
        x.image().bind(item_portrait, question.getPortrait_url(),options);
        comment_icon.setText(question.getComment_count() + "");
        share_icon.setText(question.getShare_count() + "");
        question_id = question.getQuestion_id();
        bt_add_comment.setOnClickListener(this);
        top_back.setOnClickListener(this);
        if (isQuestion) {
            x.image().bind(image_text_item_img_right, question.getRight_url());
//            image_text_item_img_left.setImageDrawable(question.getImage_left());
            x.image().bind(image_text_item_img_left, question.getLeft_url());
//            image_text_item_img_right.setImageDrawable(question.getImage_right());

        } else {
            image_text_item_img_left.setVisibility(View.GONE);
            image_text_item_img_right.setVisibility(View.GONE);
            gsyVideoPlayer_left.setVisibility(View.VISIBLE);
            gsyVideoPlayer_right.setVisibility(View.VISIBLE);
            initVideoPlayer(gsyVideoPlayer_left, question.getLeft_url());
            initVideoPlayer(gsyVideoPlayer_right, question.getRight_url());
//            getCommentListfromServer(isQuestion);
        }
        getCommentListfromServer(isQuestion);


    }


    private void initVideoPlayer(final StandardGSYVideoPlayer gsyVideoPlayer, String url) {

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(utils.createVideoThumbnail(url));
        gsyVideoPlayer.setThumbImageView(imageView);
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
        super.onBackPressed();
        if (!isQuestion) {
            //先返回正常状态
            if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                gsyVideoPlayer_left.getFullscreenButton().performClick();
                gsyVideoPlayer_right.getFullscreenButton().performClick();
                return;
            }
            //释放所有
            gsyVideoPlayer_left.setStandardVideoAllCallBack(null);
            gsyVideoPlayer_right.setStandardVideoAllCallBack(null);
            GSYVideoPlayer.releaseAllVideos();
            if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                super.onBackPressed();
            } else {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }

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
                if (bt_add_comment.getText().equals("+")) {
                    hint_new_comment.setVisibility(View.GONE);
                    et_add_comment_content.setVisibility(View.VISIBLE);
                    bt_add_comment.setText("提交");
                } else {
                    comment_content = et_add_comment_content.getText().toString();
                    if (!("".equals(comment_content))) {
                        pushComment();
                        hint_new_comment.setVisibility(View.VISIBLE);
                        et_add_comment_content.setVisibility(View.GONE);
                        et_add_comment_content.setText("");
                        bt_add_comment.setText("+");
                    } else {
                        Toast.makeText(this, "你没有评论哦", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.top_back:
                finish();
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
                JSONObject temp = jsonArray.getJSONObject(i);
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
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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