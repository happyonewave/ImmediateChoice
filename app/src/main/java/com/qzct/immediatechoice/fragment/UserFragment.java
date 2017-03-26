package com.qzct.immediatechoice.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.CommentActivity;
import com.qzct.immediatechoice.activity.LoginActivity;
import com.qzct.immediatechoice.activity.SettingActivity;
import com.qzct.immediatechoice.adpter.UserAdpter;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserFragment extends baseFragment implements View.OnClickListener {
    private static final String TAG = "qin";
    private View v;
    List<Question> questionList;
    GridView lv;
    User user;
    private String portrait_path;
    private TextView tv_username;
    private ImageView blurImageView;
    private ImageView user_portrait;
    private ImageView bt_setting;
    private TextView hint_mypush;

    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {
        v = v.inflate(context, R.layout.fragment_user, null);
        user = MyApplication.user;
        tv_username = (TextView) v.findViewById(R.id.user_tv_username);
        blurImageView = (ImageView) v.findViewById(R.id.iv_bg);
        user_portrait = (ImageView) v.findViewById(R.id.user_portrait);
        lv = (GridView) v.findViewById(R.id.gv_user);
        bt_setting = (ImageView) v.findViewById(R.id.bt_setting);
        hint_mypush = (TextView) v.findViewById(R.id.hint_mypush);
        return v;
    }

    @Override
    public void initdata() {
//        CircleImageView user_portrait = (CircleImageView)v.findViewById(R.id.user_portrait);
//        SmartImageView user_portrait = (SmartImageView) v.findViewById(R.id.user_portrait);
//        RelativeLayout rl_bg =  (RelativeLayout) v.findViewById(R.id.rl_bg);
//        ImageOptions options = new ImageOptions.Builder().setCircular(true)
//                .setFailureDrawableId(R.mipmap.default_portrait).build();
//        x.image().bind(user_portrait, user.getPortrait_path(), options);
        portrait_path = user.getPortrait_path();
//        if (MyApplication.logined) {
//            portrait_path = user.getPortrait_path();
//        } else {
//            portrait_path = utils.getUribyId(getActivity(), R.mipmap.default_portrait).toString();
//
//        }
        Glide.with(this).load(portrait_path)
                .bitmapTransform(new BlurTransformation(context, 25), new CenterCrop(context))
                .into(blurImageView);


        Glide.with(this).load(portrait_path)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(user_portrait);

        tv_username.setText(user.getUsername());
        user_portrait.setOnClickListener(this);
        bt_setting.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = questionList.get(position);
                MyApplication.question = question;
                if (question.getLeft_url().contains("image")) {
                    MyApplication.isQuestion = true;
                } else {
                    MyApplication.isQuestion = false;
                }
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
        if (MyApplication.logined) {
            hint_mypush.setVisibility(View.VISIBLE);
            getMyPush();
        }


//        ShowConversationFromJsonArrayTask ShowConversationFromJsonArrayTask = new ShowConversationFromJsonArrayTask(context, lv, MyApplication.url_user);
//        ShowConversationFromJsonArrayTask.execute();
    }

    public void getMyPush() {
        RequestParams entity = new RequestParams(Config.url_user);
        entity.addBodyParameter("quizzer_name", user.getUsername());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                ArrayList<Question> questionArrayList = null;
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    //new一个question数组
                    questionList = new ArrayList<Question>();
                    //遍历传入的jsonArray
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = jsonArray.getJSONObject(i);
                        //读取相应内容
                        int question_id = temp.getInt("question_id");
                        String post_time = temp.getString("post_time");
                        String question_content = temp.getString("question_content");
                        String left_url = temp.getString("left_url");
                        String right_url = temp.getString("right_url");
                        String quizzer_name = temp.getString("quizzer_name");
                        String portrait_url = temp.getString("portrait_url");
                        int share_count = temp.getInt("share_count");
                        int comment_count = temp.getInt("comment_count");
                        String comment = temp.getString("comment");

                        Question Question = new Question(question_id, question_content,
                                left_url, right_url, quizzer_name, portrait_url,
                                share_count, comment_count, comment, null, post_time);
                        System.out.println(Question.toString());
                        questionList.add(Question);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("空1", context.toString());

                Log.i("空2", lv.toString());
                //设置适配器
                lv.setAdapter(new UserAdpter(context, questionList));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setting:
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.user_portrait:
                Intent login = new Intent(context, LoginActivity.class);
                startActivity(login);
                break;
        }
    }


//    class ShowConversationFromJsonArrayTask extends AsyncTask<String, String, JSONArray> {
//
//        String spec;
//        Context context;
//        GridView listView;
//
//        public ShowConversationFromJsonArrayTask(Context context, GridView listView, String spec) {
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
//            ArrayList<conversation> conversationlist = null;
//            try {
//                //new一个info数组
//                conversationlist = new ArrayList<conversation>();
//                System.out.println("for前");
//                //遍历传入的jsonArray
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject temp = jsonArray.getJSONObject(i);
//                    //读取相应内容
//                    String content = temp.getString("content");
//                    String portraiturl = temp.getString("portraiturl");
//                    String addresser = temp.getString("addresser");
//                    String addressee = temp.getString("addressee");
//
//                    conversation conversation = new conversation(content, portraiturl, addressee, addresser);
//                    System.out.println(conversation.toString());
//                    conversationlist.add(conversation);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Log.i("空1", context.toString());
//
//            Log.i("空2", listView.toString());
//            //设置适配器
//            listView.setAdapter(new UserAdpter(context, conversationlist));
//        }
//    }


//	Handler handler =  new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//				System.out.println("连接成功");
//				JSONArray jsonArray =(JSONArray) msg.obj;
//				//显示
//				show(jsonArray);
//		}
//
//		private void show(JSONArray jsonArray) {
//
//			// TODO Auto-generated method stub
//				//通过资源id拿到listview对象
//			 GridView lv =(GridView) v.findViewById(R.id.lv_chat);
//				try {
//					//new一个info数组
//					conversationlist = new ArrayList<conversation>();
//					System.out.println("for前");
//					//遍历传入的jsonArray
//					for(int i=0;i<jsonArray.length();i++){
//						JSONObject temp = jsonArray.getJSONObject(i);
//						//读取相应内容
//						String content = temp.getString("content");
//						String portraiturl = temp.getString("portraiturl");
//						String addresser = temp.getString("addresser");
//						String addressee = temp.getString("addressee");
//
//						conversation conversation = new conversation(content, portraiturl, addressee, addresser);
//						System.out.println(conversation.toString());
//						conversationlist.add(conversation);
//
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				//设置适配器
//				lv.setAdapter(new UserAdpter(context,conversationlist));
//		}
//	};
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//拿到json
//		GetJsonarray(getString(R.string.url_user));
//	}
//
//	private void GetJsonarray(final String spec) {
//		//开一个子线程
//		new Thread(){
//			 Message  msg = Message.obtain();
//			public void run() {
//				JSONArray Array = utils.GetJsonArray(spec);
//				msg.obj = Array;
//				handler.sendMessage(msg);
//			};
//		}.start();
//	}


}



	