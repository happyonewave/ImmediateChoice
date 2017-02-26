package com.qzct.immediatechoice.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.itheima.immediatechoice.R;


/**
 * Created by Administrator on 2017-02-26.
 */
public class RegisterFirstFragment extends baseFragment {
    @Override
    public View initview() {
        final View v = View.inflate(context, R.layout.fill_tel, null);
        Button bt_register_one_next = (Button) v.findViewById(R.id.bt_register_one_next);
        bt_register_one_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {
                Toast.makeText(context,"下一步已点击",Toast.LENGTH_LONG).show();
                EditText et_tel = (EditText) v.findViewById(R.id.et_tel);
                String phone_number = et_tel.getText().toString();
//                String phone_number = "15158442585";

                RegisterSecondFragment Second = new RegisterSecondFragment(phone_number);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_register, Second);
//                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;
    }

    @Override
    public void initdata() {


    }
}
