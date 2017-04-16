package com.qzct.immediatechoice.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qzct.immediatechoice.R;


/**
 * Created by Administrator on 2017-02-26.
 */
public class RegisterFirstFragment extends baseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        final View v = View.inflate(context, R.layout.fill_tel, null);
        Button bt_register_one_next = (Button) v.findViewById(R.id.bt_register_one_next);
        bt_register_one_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {
                Toast.makeText(context,"下一步已点击",Toast.LENGTH_LONG).show();
                EditText et_tel = (EditText) v.findViewById(R.id.et_tel);
                String phone_number = et_tel.getText().toString();

                RegisterSecondFragment Second = new RegisterSecondFragment(phone_number);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_register, Second);
                transaction.commit();
            }
        });


        return v;
    }

    @Override
    public void initData() {


    }
}
