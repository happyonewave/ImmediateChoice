package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.domain.info;

import java.util.List;

/**
 * Created by qin on 2017/2/21.
 */

public 	class DiscoveryAdpter extends BaseAdapter{


    Context context ;
    List<info> infolist;

    public DiscoveryAdpter(Context context, List<info> infolist) {
        this.context = context;
        this.infolist = infolist;
    }

    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null ;
        if(convertView == null){
            v = View.inflate(context, R.layout.discoveryfragment_item, null);//将fragment01_item填充成一个View
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