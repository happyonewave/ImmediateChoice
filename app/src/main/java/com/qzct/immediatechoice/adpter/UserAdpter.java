package com.qzct.immediatechoice.adpter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itheima.immediatechoice.R;
import com.loopj.android.image.SmartImageView;
import com.qzct.immediatechoice.domain.conversation;

import java.util.List;

/**
 * Created by qin on 2017/2/21.
 */

public 	class UserAdpter extends BaseAdapter {

    String ListviewName ;
    List<conversation> conversationlist;
    Context context ;
    public UserAdpter(Context centext ,List<conversation> conversationlist) {
        this.conversationlist = conversationlist;
        this.context = centext;
    }

    @Override
    public int getCount() {
        return conversationlist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null ;
        Log.i("now","getView");
        conversation conversation = conversationlist.get(position);
        if(convertView == null){
//				v =  new LinearLayout(context);
//				v.setOrientation(LinearLayout.VERTICAL);
            if(conversation.getAddresser().equals( "小梨子")){
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