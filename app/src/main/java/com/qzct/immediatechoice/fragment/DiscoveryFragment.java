package com.qzct.immediatechoice.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qzct.immediatechoice.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.adpter.DiscoveryAdpter;
import com.qzct.immediatechoice.domain.info;
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

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;


@ContentView(R.layout.fragment_discovery)
public class DiscoveryFragment extends baseFragment {

    private View v;
    @ViewInject(R.id.discovery_banner)
    private BGABanner banner;
    @ViewInject(R.id.discovery_search)
    private EditText discovery_search;
    @ViewInject(R.id.discovery_scan)
    private ImageView discovery_scan;


    @Override
    public View initview(LayoutInflater inflater, ViewGroup container) {

//        v = View.inflate(getActivity(), R.layout.fragment_discovery, null);
        v = x.view().inject(this, inflater, container);
        return v;
    }

    @Override
    public void initdata() {
        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_1));
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_2));
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_3));
        views.add(BGABannerUtil.getItemImageView(context, R.mipmap.carousel_4));
        banner.setData(views);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        final ListView lv = (ListView) v.findViewById(R.id.lv);
        ShowFromJsonArrayTask showFromJsonArrayTask = new ShowFromJsonArrayTask(context, lv, MyApplication.url_Discovery);
        showFromJsonArrayTask.execute();
        final SwipeRefreshLayout swipe_refresh = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        final TextView tv_swipe_refresh = (TextView) v.findViewById(R.id.tv_swipe_refresh);

        swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.apporange), Color.BLUE);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                tv_swipe_refresh.setVisibility(View.VISIBLE);
                ShowFromJsonArrayTask showFromJsonArrayTask = new ShowFromJsonArrayTask(context, lv, MyApplication.url_Discovery);
                showFromJsonArrayTask.execute();
                tv_swipe_refresh.setText(R.string.swipe_refreshing);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        tv_swipe_refresh.setText(R.string.swipe_refresh_finish);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tv_swipe_refresh.setVisibility(View.GONE);
                            }
                        }, 1000);
                        swipe_refresh.setRefreshing(false);

//					tv_swipe_refresh.setText(R.string.tv_swipe_refresh_text);
                    }
                }, 1500);
            }
        });

    }
    @Event(R.id.discovery_scan)
    private  void  click(View v) {
        Toast.makeText(context, "点击了discovery_scan", Toast.LENGTH_SHORT).show();
    }

    class ShowFromJsonArrayTask extends AsyncTask<String, String, JSONArray> {

        String spec;
        Context context;
        ListView listView;

        public ShowFromJsonArrayTask(Context context, ListView listView, String spec) {
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
            ArrayList<info> infolist = null;
            try {
                //new一个info数组
                infolist = new ArrayList<info>();
                //遍历传入的jsonArray
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    //读取相应内容
                    String student = temp.getString("student");
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
            listView.setAdapter(new DiscoveryAdpter(context, infolist));
            super.onPostExecute(jsonArray);
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

//		private void show(JSONArray jsonArray) {
//
//				//通过资源id拿到listview对象
//			 ListView lv = (ListView) v.findViewById(R.id.lv);
//				try {
//					//new一个info数组
//					infolist = new ArrayList<info>();
//					System.out.println("for前");
//					System.out.println(jsonArray);
//					//遍历传入的jsonArray
//					for(int i=0;i<jsonArray.length();i++){
//						System.out.println("for内");
//						JSONObject temp = jsonArray.getJSONObject(i);
//						System.out.println(temp);
//						//读取相应内容
//						String student = temp.getString("student");
//						System.out.println("student" + student);
//						String date = temp.getString("date");
//						String imageurl = temp.getString("imageurl");
//
//						info info = new info(student, date, imageurl);
//						System.out.println(info.toString());
//						infolist.add(info);
//
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				//设置适配器
//				lv.setAdapter(new DiscoveryAdpter(context,infolist));
//		}
//	};


//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//拿到json
//		GetJsonarray(getString(R.string.url_Discovery));
//	}

//	private void GetJsonarray(final String spec) {
//		// TODO Auto-generated method stub
//		//开一个子线程
//		new Thread(){
//			 Message  msg = Message.obtain();
//			public void run() {
//				JSONArray Array = utils.GetJsonArray(spec);
//				System.out.println("Array" +  Array);
//				msg.obj = Array;
//				handler.sendMessage(msg);
//			};
//		}.start();
//	}


}

