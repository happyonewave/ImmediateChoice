package com.qzct.immediatechoice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.CommentAdpter;
import com.qzct.immediatechoice.domain.Comment;
import com.qzct.immediatechoice.pager.ImageTextPager;
import com.qzct.immediatechoice.util.Config;

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

public class Comment_dialog extends Dialog implements View.OnClickListener, Callback.CommonCallback<String> {

    private ImageTextPager.ItemData itemData;
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
    TextView hint_new_comment;
    TextView et_add_comment_content;
    String comment_content;
    int question_id;
    private JSONArray jsonArray;
    private String PUSH_COMMENT = "0";


    @Override
    protected void onCreate(Bundle savedInstanceStat) {
        setContentView(R.layout.dialog_comment);
//        setTitle("评价");

        itemData = MyApplication.imageTextItemData;
        initView();
        initData();

    }


    /**
     * 用主题样式来设置无无标题栏
     *
     * @param context
     * @param theme
     */
    public Comment_dialog(Context context, int theme) {
        super(context, theme);

    }

    private void initView() {
        tv_question = (TextView) findViewById(R.id.tv_question);    //拿到相应的View对象
        image_text_item_img_left = (SmartImageView) findViewById(R.id.image_text_item_img_left);
        image_text_item_img_right = (SmartImageView) findViewById(R.id.image_text_item_img_right);
        item_username = (TextView) findViewById(R.id.item_username);
        item_portrait = (ImageView) findViewById(R.id.item_portrait);
        comment_icon = (Button) findViewById(R.id.comment_icon);
        share_icon = (Button) findViewById(R.id.share_icon);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        hint_new_comment = (TextView) findViewById(R.id.hint_new_comment);
        et_add_comment_content = (TextView) findViewById(R.id.et_add_comment_content);
        bt_add_comment = (Button) findViewById(R.id.bt_add_comment);
    }

    private void initData() {
        tv_question.setText(itemData.getQuestion_content());
        image_text_item_img_left.setImageDrawable(itemData.getImage_left());
        image_text_item_img_right.setImageDrawable(itemData.getImage_right());
        item_username.setText(itemData.getUsername());
        item_portrait.setImageDrawable(itemData.getPortrait());
        comment_icon.setText(itemData.getComment_num());
        share_icon.setText(itemData.getShare_num());
        bt_add_comment.setOnClickListener(this);
        question_id = itemData.getQuestion_id();
        getCommentListfromServer();


    }

    /**
     * 从服务器获取commentList
     */
    private void getCommentListfromServer() {
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
        //判断提交按钮的状态
        if (bt_add_comment.getText().equals("+")) {
            hint_new_comment.setVisibility(View.GONE);
            et_add_comment_content.setVisibility(View.VISIBLE);
            bt_add_comment.setText("提交");
        } else {
            comment_content = et_add_comment_content.getText().toString();
            if (!("".equals(comment_content))){
                pushComment();
                hint_new_comment.setVisibility(View.VISIBLE);
                et_add_comment_content.setVisibility(View.GONE);
                et_add_comment_content.setText("");
                bt_add_comment.setText("+");
            }else{
                Toast.makeText(getContext(), "你没有评论哦", Toast.LENGTH_SHORT).show();
            }
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
            adpter = new CommentAdpter(getContext(), commentList);
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
                getCommentListfromServer();
                Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
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