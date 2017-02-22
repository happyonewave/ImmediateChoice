package com.qzct.immediatechoice;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.adpter.DiscoveryAdpter;
import com.qzct.immediatechoice.domain.info;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryFragment extends baseFragment {

	private View v;
	List<info> infolist;
	
	Handler handler =  new Handler(){
		@Override
		public void handleMessage(Message msg) {
				System.out.println("连接成功");
				JSONArray jsonArray =(JSONArray) msg.obj;
				//显示
				show(jsonArray);
		}
					
		private void show(JSONArray jsonArray) { 										

				//通过资源id拿到listview对象
			 ListView lv = (ListView) v.findViewById(R.id.lv);
				try {
					//new一个info数组
					infolist = new ArrayList<info>();
					System.out.println("for前");
					System.out.println(jsonArray);
					//遍历传入的jsonArray
					for(int i=0;i<jsonArray.length();i++){
						System.out.println("for内");
						JSONObject temp = jsonArray.getJSONObject(i);
						System.out.println(temp);
						//读取相应内容
						String student = temp.getString("student");
						System.out.println("student" + student);
						String date = temp.getString("date");
						String imageurl = temp.getString("imageurl");

						info info = new info(student, date, imageurl);
						System.out.println(info.toString());
						infolist.add(info);
						
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//设置适配器
				lv.setAdapter(new DiscoveryAdpter(context,infolist));
		}
	};

	@Override
	public View initview() {
		v = View.inflate(getActivity(), R.layout.discoveryfragment, null);
		final SwipeRefreshLayout swipe_refresh = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh);
		final TextView tv_swipe_refresh = (TextView)v.findViewById(R.id.tv_swipe_refresh);
		swipe_refresh.setColorSchemeColors(R.color.apporange,Color.BLUE);
		swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				tv_swipe_refresh.setVisibility(View.VISIBLE);
				tv_swipe_refresh.setText(R.string.swipe_refreshing);
				GetJsonarray(getString(R.string.url_Discovery));
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
					tv_swipe_refresh.setText(R.string.swipe_refresh_finish);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								tv_swipe_refresh.setVisibility(View.GONE);
							}
						},1000);
					swipe_refresh.setRefreshing(false);

//					tv_swipe_refresh.setText(R.string.tv_swipe_refresh_text);
					}
				},1500);
			}
		});
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//拿到json
		GetJsonarray(getString(R.string.url_Discovery));
	}

	private void GetJsonarray(final String spec) {
		// TODO Auto-generated method stub
		//开一个子线程
		new Thread(){
			 Message  msg = Message.obtain();
			public void run() {
				JSONArray Array = utils.GetJsonArray(spec);
				System.out.println("Array" +  Array);
				msg.obj = Array;
				handler.sendMessage(msg);
			};
		}.start();
	}



}
