package com.qzct.immediatechoice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.immediatechoice.R;
import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.domain.info;
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//设置适配器
				lv.setAdapter(new MyAdpter());
		}
	};



	@Override
	public View initview() {
		v = View.inflate(getActivity(), R.layout.discoveryfragment, null);

		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//拿到json
		GetJsonarray(getString(R.string.serverurl));
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

	class MyAdpter extends BaseAdapter{


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infolist.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = null ; 
			if(convertView == null){
				v = View.inflate(getActivity(), R.layout.discoveryfragment_item, null);//将fragment01_item填充成一个View
				System.out.println("调用：" + position);
			}else{
				v = convertView;
			}
				
				
			TextView tv_student =  (TextView) v.findViewById(R.id.tv_student);	//拿到相应的View对象
			TextView tv_date =  (TextView) v.findViewById(R.id.tv_date);
			SmartImageView siv_item =   (SmartImageView) v.findViewById(R.id.siv_item);

			info i = infolist.get(position);									//拿到一个info对象
				
			tv_student.setText(i.getStudent());									//设置相应的信息
			tv_date.setText(i.getDate());
			System.out.println(i.getImageurl());
			siv_item.setImageUrl(i.getImageurl());
			return v;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

}
