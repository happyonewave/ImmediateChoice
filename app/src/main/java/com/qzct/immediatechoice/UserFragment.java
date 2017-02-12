package com.qzct.immediatechoice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.immediatechoice.R;
import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.domain.conversation;
import com.qzct.immediatechoice.util.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserFragment extends baseFragment {
	
	private View v;
	List<conversation> conversationlist;



	@Override
	public View initview() {
		v =  v.inflate(context, R.layout.userfragment, null);
////	ll = new LinearLayout(context);
////	lv = new ListView(context);
//		ll.addView(lv);
		
		
//		TextView tv = new TextView(context);
//		tv.setText("交流");

		return v;
	}

	Handler handler =  new Handler(){
		@Override
		public void handleMessage(Message msg) {
				System.out.println("连接成功");
				JSONArray jsonArray =(JSONArray) msg.obj;
				//显示
				show(jsonArray);
		}
					
		private void show(JSONArray jsonArray) { 										

			// TODO Auto-generated method stub
				//通过资源id拿到listview对象
			 ListView lv =(ListView) v.findViewById(R.id.lv_chat);
				try {
					//new一个info数组
					conversationlist = new ArrayList<conversation>();
					System.out.println("for前");
					//遍历传入的jsonArray
					for(int i=0;i<jsonArray.length();i++){
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//设置适配器
				lv.setAdapter(new MyAdpter());
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//拿到json
		GetJsonarray(getString(R.string.serverurl_conversation));
	}
					
	private void GetJsonarray(final String spec) {
		//开一个子线程
		new Thread(){
			 Message  msg = Message.obtain();
			public void run() {
				JSONArray Array = utils.GetJsonArray(spec);
				msg.obj = Array;
				handler.sendMessage(msg);
			};
		}.start();
	}

	class MyAdpter extends BaseAdapter{


		@Override
		public int getCount() {
			return conversationlist.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null ; 
			conversation conversation = conversationlist.get(position);		
			if(convertView == null){
//				v =  new LinearLayout(context);
//				v.setOrientation(LinearLayout.VERTICAL);
				if(conversation.getAddresser().equals( "小王")){
					v =  v.inflate(context, R.layout.userfragment_item_lift, null);
				}else{
					v =  v.inflate(context, R.layout.userfragment_item_right, null);
				}
				System.out.println("调用：" + position);
			}else{
				v =  convertView;
			}
				

//			SmartImageView siv = new SmartImageView(context);
//			TextView tv_content = new TextView(context);
//			TextView tv_addresser = new TextView(context);
//			TextView tv_addressee = new TextView(context);
//			siv.setRight(0);

			SmartImageView siv =(SmartImageView) v.findViewById(R.id.siv_portrait);
			TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
			TextView tv_addresser = (TextView) v.findViewById(R.id.tv_addresser);
			TextView tv_addressee = (TextView) v.findViewById(R.id.tv_addressee);
							//拿到一个info对象
				
			tv_content.setText(conversation.getContent());	
			tv_addresser.setText(conversation.getAddresser());	
			tv_addressee.setText(conversation.getAddressee());	
			System.out.println(conversation.getPortraiturl());
			siv.setImageUrl(conversation.getPortraiturl());
//			v.addView(siv);
//			v.addView(tv_content);
//			v.addView(tv_addresser);
//			v.addView(tv_addressee);
			return v;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	
}


	