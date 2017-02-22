package com.qzct.immediatechoice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.itheima.immediatechoice.R;

public class SettingActivity extends Activity {

	private FrameLayout fl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		ImageView iv_back = (ImageView) findViewById(R.id.seting_back);
		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SettingActivity.this,MainActivity.class);
			startActivity(intent);
			}
		});
}
}