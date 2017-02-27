package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.SettingActivity;
import com.qzct.immediatechoice.adpter.UserAdpter;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.domain.conversation;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends baseFragment {

    private View v;
    List<conversation> conversationlist;


    @Override
    public View initview() {
        v = v.inflate(context, R.layout.fragment_user, null);
////	ll = new LinearLayout(context);
////	lv = new ListView(context);
//		ll.addView(lv);


//		TextView tv = new TextView(context);
//		tv.setText("交流");

        return v;
    }

    @Override
    public void initdata() {

        User user = myApplication.getUser();
        TextView tv_username = (TextView) v.findViewById(R.id.user_tv_username);
        tv_username.setText(user.getUsername());
        final GridView lv = (GridView) v.findViewById(R.id.gv_user);
        ShowConversationFromJsonArrayTask ShowConversationFromJsonArrayTask = new ShowConversationFromJsonArrayTask(context, lv, getString(R.string.url_user));
        ShowConversationFromJsonArrayTask.execute();
        Button bt_setting = (Button) v.findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
            }
        });
    }


    class ShowConversationFromJsonArrayTask extends AsyncTask<String, String, JSONArray> {

        String spec;
        Context context;
        GridView listView;

        public ShowConversationFromJsonArrayTask(Context context, GridView listView, String spec) {
            this.context = context;
            this.listView = listView;
            this.spec = spec;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            //返回获取的jasonArray
            return utils.GetJsonArray(spec);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            ArrayList<conversation> conversationlist = null;
            try {
                //new一个info数组
                conversationlist = new ArrayList<conversation>();
                System.out.println("for前");
                //遍历传入的jsonArray
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    //读取相应内容
                    String content = temp.getString("content");
                    String portraiturl = temp.getString("portraiturl");
                    String addresser = temp.getString("addresser");
                    String addressee = temp.getString("addressee");

                    conversation conversation = new conversation(content, portraiturl, addressee, addresser);
                    System.out.println(conversation.toString());
                    conversationlist.add(conversation);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("空1", context.toString());

            Log.i("空2", listView.toString());
            //设置适配器
            listView.setAdapter(new UserAdpter(context, conversationlist));
        }
    }

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



	