package com.qzct.immediatechoice.fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qzct.immediatechoice.R;


/**
 * Created by Administrator on 2017-02-26.
 */
public class RegisterFirstFragment extends baseFragment {
    View v;
    private int user_type = -1;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        v = View.inflate(context, R.layout.fill_tel, null);


        return v;
    }

    @Override
    public void initData() {
        Button bt_register_one_next = (Button) v.findViewById(R.id.bt_register_one_next);
        AppCompatSpinner spinner = (AppCompatSpinner) v.findViewById(R.id.spinner);
        bt_register_one_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bt_v) {
                Toast.makeText(context, "下一步已点击", Toast.LENGTH_LONG).show();
                EditText et_tel = (EditText) v.findViewById(R.id.et_tel);
                String phone_number = et_tel.getText().toString();
                if (!isEmail(phone_number)) {
                    Toast.makeText(context, "请填写正确的邮箱地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user_type == -1) {
                    Toast.makeText(context, "请选择用户类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterSecondFragment Second = new RegisterSecondFragment(phone_number, user_type);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fl_register, Second);
                transaction.commit();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        user_type = 1;
                        break;
                    case 2:
                        user_type = 0;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean isEmail(String email) {
        if (email.contains("@") && email.contains(".")) {
            return true;
        }
        return false;
    }
}
