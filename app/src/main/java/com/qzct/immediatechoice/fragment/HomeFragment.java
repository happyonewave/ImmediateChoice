package com.qzct.immediatechoice.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.MainActivity;
import com.qzct.immediatechoice.activity.PushActivity;
import com.qzct.immediatechoice.activity.QuestionnaireActivity;
import com.qzct.immediatechoice.pager.AttentionPager;
import com.qzct.immediatechoice.pager.BasePager;
import com.qzct.immediatechoice.pager.ImageTextPager;
import com.qzct.immediatechoice.pager.VideoPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
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
    LinearLayout fab_question_layout;
    private ViewPager vp_home;
    private PagerAdapter pagerAdapter = null;
    private List<BasePager> pagers = new ArrayList<BasePager>();
    View home_image_text_line;
    View home_video_line;
    View home_attention_line;
    FloatingActionButton fab_home;
    FloatingActionButton fab_push;
    FloatingActionButton fab_question;
    public final static String ACTION_SET_FAB_VISBILITY = "ACTION_SET_FAB_VISBILITY";
    private LinearLayout home_title;

    /**
     * 填充view
     *
     * @return
     */
    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        v = x.view().inject(this, inflater, container);
        vp_home = (ViewPager) v.findViewById(R.id.vp_home);
        fab_push_vote__layout = (LinearLayout) v.findViewById(R.id.fab_push_vote__layout);
        fab_question_layout = (LinearLayout) v.findViewById(R.id.fab_question_layout);
        home_image_text_line = v.findViewById(R.id.home_image_text_line);
        home_video_line = v.findViewById(R.id.home_video_line);
        home_attention_line = v.findViewById(R.id.home_attention_line);

        home_title = (LinearLayout) v.findViewById(R.id.home_title);
        fab_home = (FloatingActionButton) v.findViewById(R.id.fab_home);
        fab_push = (FloatingActionButton) v.findViewById(R.id.fab_push);
        fab_question = (FloatingActionButton) v.findViewById(R.id.fab_question);
        fab_home.setOnClickListener(this);
        fab_push.setOnClickListener(this);
        fab_question.setOnClickListener(this);
        return v;
    }

    /**
     * 设置悬浮按钮的可见属性
     *
     * @param isvisible 是否可见
     */
    public void setFabvisibility(boolean isvisible) {

        if (isvisible) {
            fab_home.show();
            fab_push.show();
            fab_question.show();
            home_title.setVisibility(View.VISIBLE);
            MainActivity.rg_nav.setVisibility(View.VISIBLE);
            Log.e("show", "show");
        } else {
            fab_home.hide();
            fab_push.hide();
            fab_question.hide();
            home_title.setVisibility(View.GONE);
            MainActivity.rg_nav.setVisibility(View.GONE);
            Log.e("hide", "hide");


        }


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

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //主悬浮按钮
            case R.id.fab_home:

                if (fab_push_vote__layout.getVisibility() == View.GONE) {
                    fab_push_vote__layout.setVisibility(View.VISIBLE);
                    fab_question_layout.setVisibility(View.VISIBLE);
                } else {
                    fab_push_vote__layout.setVisibility(View.GONE);
                    fab_question_layout.setVisibility(View.GONE);
                }
                break;
            //发起
            case R.id.fab_push:
                Intent intent = new Intent(context, PushActivity.class);
                startActivity(intent);
                break;
            //问卷调查
            case R.id.fab_question:
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
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SET_FAB_VISBILITY);

        //接收并处理是否显示悬浮按钮的广播
        BroadcastReceiver bordcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //信息处理

                Log.e("isvisible", String.valueOf(intent.getBooleanExtra("isvisible", true)));

                setFabvisibility(intent.getBooleanExtra("isvisible", true));


            }
        };
        broadcastManager.registerReceiver(bordcastReceiver, intentFilter);

        pagers.add(new ImageTextPager(context));
        pagers.add(new VideoPager(getActivity()));
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
//            ArrayList<Question> questionlist = null;
//            try {
//                //new一个info数组
//                questionlist = new ArrayList<Question>();
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
//                    Question Question = new Question(question_content, image_left, image_right, quizzer_name, share_count, comment_count, comment, null);
//                    System.out.println(Question.toString());
//                    questionlist.add(Question);
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
