package com.qzct.immediatechoice;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itheima.immediatechoice.R;

public class FunctionFragment extends baseFragment {

    private View v;


    @Override
    public View initview() {
        v = View.inflate(getActivity(), R.layout.functionfragment, null);
//		TextView tv = new TextView(context);
//		tv.setText("首页");
        ViewPager vp_home = (ViewPager) v.findViewById(R.id.vp_home);
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
