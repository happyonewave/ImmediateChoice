package com.qzct.immediatechoice.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.activity.RegisterActivity;

/**
 * Created by Administrator on 2017-02-26.
 */
@SuppressLint("ValidFragment")
public class RegisterSecondFragment extends baseFragment {
    String phone_number;

    @SuppressLint("ValidFragment")
    public RegisterSecondFragment(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        registerActivity.setTitleColor(0);
        final View v = View.inflate(context, R.layout.fill_verification_code, null);
        Button bt_register_two_next = (Button) v.findViewById(R.id.bt_register_two_next);
        bt_register_two_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {
                Toast.makeText(context, "下一步已点击", Toast.LENGTH_LONG).show();

                EditText et_verification = (EditText) v.findViewById(R.id.et_verification);
                String verification = et_verification.getText().toString();
                if (verification.equals("153698")){
                    RegisterFinallyFragment finish = new RegisterFinallyFragment(phone_number);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fl_register, finish);
                    transaction.commit();
                }else{
                    Toast.makeText(context,"验证码错误，请重新获取验证",Toast.LENGTH_LONG).show();
                }
            }
        });


        return v;


    }

    @Override
    public void initData() {

    }
}
