package com.qzct.immediatechoice.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.pager.ImageTextPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * 评论
 */
@ContentView(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity {

    private ImageTextPager.ItemData itemData;
    TextView tv_question;
    SmartImageView image_text_item_img_left;
    SmartImageView image_text_item_img_right;
    TextView item_username;
    ImageView item_portrait;
    Button comment_icon;
    Button share_icon;
    TextView item_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("评论");
//        itemData = (ImageTextPager.ItemData) getIntent().getSerializableExtra("itemData");
        itemData = MyApplication.itemData;

        initView();
        initData();

    }

    private void initView() {
        tv_question = (TextView) findViewById(R.id.tv_question);    //拿到相应的View对象
        image_text_item_img_left = (SmartImageView) findViewById(R.id.image_text_item_img_left);
        image_text_item_img_right = (SmartImageView) findViewById(R.id.image_text_item_img_right);
        item_username = (TextView) findViewById(R.id.item_username);
        item_portrait = (ImageView) findViewById(R.id.item_portrait);
        comment_icon = (Button) findViewById(R.id.comment_icon);
        share_icon = (Button) findViewById(R.id.share_icon);
        item_comment = (TextView) findViewById(R.id.hint_new_comment);







    }

    private void initData() {
        tv_question.setText(itemData.getQuestion_content());
        image_text_item_img_left.setImageDrawable(itemData.getImage_left());
        image_text_item_img_right.setImageDrawable(itemData.getImage_right());
        item_username.setText(itemData.getUsername());
        item_portrait.setImageDrawable(itemData.getPortrait());
        comment_icon.setText(itemData.getComment_num());
        share_icon.setText(itemData.getShare_num());
        item_comment.setText(itemData.getComment());

    }
}
