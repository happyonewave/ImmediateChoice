package com.qzct.immediatechoice;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.domain.info;
import com.qzct.immediatechoice.util.StreamTools;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FunctionFragment extends baseFragment {

	private View v;




	@Override
	public View initview() {
		v = View.inflate(getActivity(), R.layout.functionfragment, null);
//		TextView tv = new TextView(context);
//		tv.setText("首页");
		ViewPager vp_home = (ViewPager)v.findViewById(R.id.vp_home);
		vp_home.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				TextView tv = new TextView(context);
				tv.setText("1");
				container.addView(tv);
				return tv;
			}
		});
		return v;
	}


}
