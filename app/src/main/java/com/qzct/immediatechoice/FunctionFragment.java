package com.qzct.immediatechoice;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.itheima.immediatechoice.R;
import com.qzct.immediatechoice.adpter.DiscoveryAdpter;
import com.qzct.immediatechoice.domain.info;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FunctionFragment extends baseFragment {

    private View v;
    ListView lv_home;

    @Override
    public View initview() {
        v = View.inflate(getActivity(), R.layout.activuty_home, null);
        return v;
    }

    @Override
    public void initdata() {

        lv_home = (ListView) v.findViewById(R.id.lv_home);
        new ShowFromJsonArrayTask(context, lv_home, getString(R.string.url_Discovery)).execute();
    }

}

class ShowFromJsonArrayTask extends AsyncTask<String, String, JSONArray> {

    String spec;
    Context context;
    ListView listView;

    public ShowFromJsonArrayTask(Context context, ListView listView, String spec) {
        this.context = context;
        this.listView = listView;
        this.spec = spec;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        //返回获取的jasonArray
        return utils.GetJsonArray(spec);
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        ArrayList<info> infolist = null;
        try {
            //new一个info数组
            infolist = new ArrayList<info>();
            //遍历传入的jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                //读取相应内容
                String student = temp.getString("student");
                String date = temp.getString("date");
                String imageurl = temp.getString("imageurl");

                info info = new info(student, date, imageurl);
                System.out.println(info.toString());
                infolist.add(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //设置适配器
        listView.setAdapter(new DiscoveryAdpter(context, infolist));
        super.onPostExecute(jsonArray);
    }
}