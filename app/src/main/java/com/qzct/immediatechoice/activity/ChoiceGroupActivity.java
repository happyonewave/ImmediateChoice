package com.qzct.immediatechoice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.GroupInfo;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qin on 2017/4/2.
 */

public class ChoiceGroupActivity extends AppCompatActivity {
    private List<GroupInfo> groupInfoList;
    private List<List<User>> membersList = new ArrayList<List<User>>();
    //    private ListView lv_group;
//    private RadioButton choice_group;
    private List<String> groupIdList = new ArrayList<String>();
    private AppCompatSpinner group_spinner;
    private TextView look_all;
    private Context context = this;
    private ArrayAdapter<String> adapter;
    private List<String> mStrList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Utils.getHasTopView(this, R.layout.activity_choicegroup, "谁能看见"));
        initView();
        initData();

    }


    private void initView() {
//        choice_group = (RadioButton) findViewById(R.id.choice_group);
//        lv_group = (ListView) findViewById(R.id.lv_group);
        group_spinner = (AppCompatSpinner) findViewById(R.id.group_spinner);
        look_all = (TextView) findViewById(R.id.look_all);

    }

    private void initData() {
//        Intent intent = new Intent(this, PushActivity.class);
//        intent.putStringArrayListExtra("groupIdList", (ArrayList<String>) groupIdList);
//        setResult(RESULT_OK, intent);
        Button new_group = new Button(getApplicationContext());
        new_group.setText("新建");
//        choice_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (lv_group.getVisibility() == View.VISIBLE) {
//                    lv_group.setVisibility(View.GONE);
//                } else {
//                    lv_group.setVisibility(View.VISIBLE);
//
//                }
//            }
//        });
        look_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mStrList.add("部分可见");
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mStrList);
        group_spinner.setAdapter(adapter);
        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 && position != mStrList.size() - 1) {
                    Intent intent = new Intent(ChoiceGroupActivity.this, PushActivity.class);
                    intent.putExtra("group_id", groupInfoList.get(position - 1).getGroup_id());
//                intent.putExtra("group_id", groupInfoList.get(position - 1).getGroup_id());
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
//                groupIdList.add(groupInfoList.get(position - 1).getGroup_id() + "");
//                Log.d("qin", groupInfoList.get(position - 1).getGroup_id() + "");
                }
                if (position == parent.getChildCount() - 1) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new  AlertDialog.Builder(getApplicationContext());

//                lv_group.addView();
            }
        });
//        lv_group.addHeaderView(new_group);
//        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(ChoiceGroupActivity.this, PushActivity.class);
//                intent.putExtra("group_id", groupInfoList.get(position - 1).getGroup_id());
//                setResult(RESULT_OK, intent);
//                finish();
////                groupIdList.add(groupInfoList.get(position - 1).getGroup_id() + "");
////                Log.d("qin", groupInfoList.get(position - 1).getGroup_id() + "");
//            }
//        });
        getGroup();
    }

    private void getGroup() {
        RequestParams entity = new RequestParams(Config.url_group);
        entity.addParameter("owner_id", 1);
//        entity.addParameter("owner_id", MyApplication.user.getUser_id());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONArray resultJsonArray = new JSONArray(result);
                        groupInfoList = GroupInfo.tolistFrom(resultJsonArray.optJSONArray(0));
//                        membersList = new ArrayList<List<User>>();
                        for (int i = 1; i < resultJsonArray.length(); i++) {
                            JSONObject temp = resultJsonArray.optJSONObject(i);
                            List<User> memberList = User.toMemberListFrom(temp.optJSONArray("members"));
                            membersList.add(memberList);
                        }
                        for (int i = 0; i < groupInfoList.size(); i++) {
                            mStrList.add(groupInfoList.get(i).getName() + "(" + membersList.get(i).size() + ")");
                        }
                        mStrList.add("从好友中选择");
                        adapter.notifyDataSetChanged();
//                        lv_group.setAdapter(new SpinnerAdapter());
//                        group_spinner.setAdapter(new SpinnerAdapter());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("qin", "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private class SpinnerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return groupInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = null;
            if (convertView != null) {
                v = (TextView) convertView;
            } else {
                v = new TextView(getApplicationContext());
            }
//            v.setCompoundDrawables(getDrawable(R.drawable.bt_sex_bg), null, null, null);
            if (position == 0) {
                v.setText("可见群组");
                return v;
            }
            v.setText(groupInfoList.get(position - 1).getName() + " (" + membersList.get(position - 1).size() + ")");
            return v;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
