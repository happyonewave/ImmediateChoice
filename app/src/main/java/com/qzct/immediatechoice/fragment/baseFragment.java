package com.qzct.immediatechoice.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qzct.immediatechoice.MyApplication;

public abstract class baseFragment extends Fragment {
    Activity context;
    MyApplication myApplication;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        myApplication = (MyApplication) context.getApplication();
        super.onCreate(savedInstanceState);
    }

    public abstract View initview(LayoutInflater inflater, ViewGroup container);

    public abstract void initdata();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = initview(inflater, container);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initdata();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (getView() != null) {
            getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
        super.setMenuVisibility(menuVisible);
    }
}
