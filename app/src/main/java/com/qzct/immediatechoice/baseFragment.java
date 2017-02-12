package com.qzct.immediatechoice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class baseFragment extends Fragment {
	Activity context;
@Override
public void onCreate(Bundle savedInstanceState) {
	context = getActivity();
	super.onCreate(savedInstanceState);
}

public abstract View initview();

@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = initview();
		return v;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
		public void setMenuVisibility(boolean menuVisible) {
		if(getView()!= null){
			getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
		}
			super.setMenuVisibility(menuVisible);
		}
}
