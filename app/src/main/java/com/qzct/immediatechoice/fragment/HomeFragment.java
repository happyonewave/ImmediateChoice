package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.qzct.immediatechoice.PushActivity;
import com.qzct.immediatechoice.QuestionnaireActivity;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.ImageTextAdpter;
import com.qzct.immediatechoice.domain.question;
import com.qzct.immediatechoice.pager.AttentionPager;
import com.qzct.immediatechoice.pager.BasePager;
import com.qzct.immediatechoice.pager.ImageTextPager;
import com.qzct.immediatechoice.pager.VideoPager;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_home)
public class HomeFragment extends baseFragment implements View.OnClickListener {

    private static final int IMAGE_TEXT = 0;
    private static final int VIDEO = 1;
    private static final int ATTENTTION = 2;
    private View v;
    LinearLayout fab_push_vote__layout;
    LinearLayout fab_questionfragment_layout;
    private ViewPager vp_home;
    private PagerAdapter pagerAdapter = null;
    private List<BasePager> pagers = new ArrayList<BasePager>();
    View home_image_text_line;
    View home_video_line;
    View home_attention_line;

    /**
     * 填充view
     *
     * @return
     */

    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
//        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        v = x.view().inject(this, inflater, container);
        vp_home = (ViewPager) v.findViewById(R.id.vp_home);
        fab_push_vote__layout = (LinearLayout) v.findViewById(R.id.fab_push_vote__layout);
        fab_questionfragment_layout = (LinearLayout) v.findViewById(R.id.fab_questionfragment_layout);
        home_image_text_line = v.findViewById(R.id.home_image_text_line);
        home_video_line = v.findViewById(R.id.home_video_line);
        home_attention_line = v.findViewById(R.id.home_attention_line);
//        Button bt_title_image = (Button) v.findViewById(R.id.bt_home_title_image);
//        Button bt_title_video = (Button) v.findViewById(R.id.bt_home_title_video);
//        Button bt_title_attention = (Button) v.findViewById(R.id.bt_home_title_attention);
//        bt_title_image.setOnClickListener(this);
//        bt_title_video.setOnClickListener(this);
//        bt_title_attention.setOnClickListener(this);
        FloatingActionButton fab_home = (FloatingActionButton) v.findViewById(R.id.fab_home);
        FloatingActionButton fab_push = (FloatingActionButton) v.findViewById(R.id.fab_push);
        FloatingActionButton fab_questionfragment = (FloatingActionButton) v.findViewById(R.id.fab_questionfragment);
        fab_home.setOnClickListener(this);
        fab_push.setOnClickListener(this);
        fab_questionfragment.setOnClickListener(this);
        return v;
    }

    /**
     * title按钮的点击事件
     * 切换到对应的View
     *
     * @param v 点击后返回的对应的对象
     */

    @Event({R.id.bt_home_title_image, R.id.bt_home_title_video, R.id.bt_home_title_attention})
    private void titleOnClick(View v) {
        reSetLine();
        switch (v.getId()) {

            case R.id.bt_home_title_image:
                vp_home.setCurrentItem(IMAGE_TEXT);
                home_image_text_line.setVisibility(View.VISIBLE);
                break;

            case R.id.bt_home_title_video:
                vp_home.setCurrentItem(VIDEO);
                home_video_line.setVisibility(View.VISIBLE);
                break;

            case R.id.bt_home_title_attention:
                vp_home.setCurrentItem(ATTENTTION);
                home_attention_line.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }

    }

    /**
     * 设置所有的Line不可见
     */
    private void reSetLine() {

        home_image_text_line.setVisibility(View.GONE);
        home_video_line.setVisibility(View.GONE);
        home_attention_line.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_home:

                if (fab_push_vote__layout.getVisibility() == View.GONE) {
                    fab_push_vote__layout.setVisibility(View.VISIBLE);
                    fab_questionfragment_layout.setVisibility(View.VISIBLE);
                } else {
                    fab_push_vote__layout.setVisibility(View.GONE);
                    fab_questionfragment_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.fab_push:
                Intent intent = new Intent(context, PushActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_questionfragment:
                intent = new Intent(context, QuestionnaireActivity.class);
                startActivity(intent);
                break;

            default:
                break;


        }
    }

    /**
     * 填充数据
     */
    @Override
    public void initdata() {

        pagers.add(new ImageTextPager(context));
        pagers.add(new VideoPager(context));
        pagers.add(new AttentionPager(context));

        /**
         * viewPager适配器
         */
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pagers.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(pagers.get(position).getRootView());


                return pagers.get(position).getRootView();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pagers.get(position).getRootView());
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

        };
        vp_home.setAdapter(pagerAdapter);
        vp_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reSetLine();
                switch (position) {
                    case IMAGE_TEXT:
                        home_image_text_line.setVisibility(View.VISIBLE);
                        break;
                    case VIDEO:
                        home_video_line.setVisibility(View.VISIBLE);
                        break;
                    case ATTENTTION:
                        home_attention_line.setVisibility(View.VISIBLE);
                        break;


                    default:

                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        lv_home = (ListView) v.findViewById(R.id.lv_home);
//        new ShowFromJsonArrayTask(context, lv_home, getString(R.string.url_image_text)).execute();
//
//        final SwipeRefreshLayout home_swipe_refresh = (SwipeRefreshLayout) v.findViewById(R.id.home_swipe_refresh);
//        home_swipe_refresh.setColorSchemeColors(Color.YELLOW, Color.BLUE);
//        home_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                Toast.makeText(context, "正在刷新", Toast.LENGTH_SHORT).show();
//                new ShowFromJsonArrayTask(context, lv_home, getString(R.string.url_image_text)).execute();
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show();
//                        home_swipe_refresh.setRefreshing(false);
//
//                    }
//                }, 1500);
//            }
//        });
    }


//
//    class ShowFromJsonArrayTask extends AsyncTask<String, String, JSONArray> {
//
//        String spec;
//        Context context;
//        ListView listView;
//
//        public ShowFromJsonArrayTask(Context context, ListView listView, String spec) {
//            this.context = context;
//            this.listView = listView;
//            this.spec = spec;
//        }
//
//        @Override
//        protected JSONArray doInBackground(String... params) {
//            //返回获取的jasonArray
//            return utils.GetJsonArray(spec);
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray jsonArray) {
//            ArrayList<question> questionlist = null;
//            try {
//                //new一个info数组
//                questionlist = new ArrayList<question>();
//                //遍历传入的jsonArray
//                for (int i = jsonArray.length() - 1; i > 0; i--) {
//                    JSONObject temp = jsonArray.getJSONObject(i);
//                    //读取相应内容
//                    String question_content = temp.getString("question_content");
//                    String image_left = temp.getString("image_left");
//                    String image_right = temp.getString("image_right");
//                    String quizzer_name = temp.getString("quizzer_name");
//                    int share_count = temp.getInt("share_count");
//                    int comment_count = temp.getInt("comment_count");
//                    String comment = temp.getString("comment");
//                    question question = new question(question_content, image_left, image_right, quizzer_name, share_count, comment_count, comment, null);
//                    System.out.println(question.toString());
//                    questionlist.add(question);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            //设置适配器
//            listView.setAdapter(new ImageTextAdpter(context, questionlist));
//            super.onPostExecute(jsonArray);
//        }
//    }

}
