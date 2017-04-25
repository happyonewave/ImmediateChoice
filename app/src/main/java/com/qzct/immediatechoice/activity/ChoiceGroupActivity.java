package com.qzct.immediatechoice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.GroupInfo;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.utils;

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
    private List<List<User>> membersList;
    private ListView lv_group;
    private RadioButton choice_group;
    private List<String> groupIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(utils.getHasTopView(this, R.layout.activity_choicegroup, "谁能看见"));
        initView();
        initData();

    }


    private void initView() {
        choice_group = (RadioButton) findViewById(R.id.choice_group);
        lv_group = (ListView) findViewById(R.id.lv_group);

    }

    private void initData() {
//        Intent intent = new Intent(this, PushActivity.class);
//        intent.putStringArrayListExtra("groupIdList", (ArrayList<String>) groupIdList);
//        setResult(RESULT_OK, intent);
        Button new_group = new Button(getApplicationContext());
        new_group.setText("新建");
        choice_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_group.getVisibility() == View.VISIBLE) {
                    lv_group.setVisibility(View.GONE);
                } else {
                    lv_group.setVisibility(View.VISIBLE);

                }
            }
        });
        new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new  AlertDialog.Builder(getApplicationContext());

//                lv_group.addView();
            }
        });
        lv_group.addHeaderView(new_group);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ChoiceGroupActivity.this, PushActivity.class);
                intent.putExtra("group_id", groupInfoList.get(position - 1).getGroup_id());
                setResult(RESULT_OK, intent);
                finish();
//                groupIdList.add(groupInfoList.get(position - 1).getGroup_id() + "");
//                Log.d("qin", groupInfoList.get(position - 1).getGroup_id() + "");
            }
        });
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
                        membersList = new ArrayList<List<User>>();
                        for (int i = 1; i < resultJsonArray.length(); i++) {
                            JSONObject temp = resultJsonArray.optJSONObject(i);
                            List<User> memberList = User.toMemberListFrom(temp.optJSONArray("members"));
                            membersList.add(memberList);
                        }
                        lv_group.setAdapter(new SpinnerAdapter());
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
            v.setText(groupInfoList.get(position).getName() + " (" + membersList.get(position).size() + ")");
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
