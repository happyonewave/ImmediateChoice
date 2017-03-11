package com.qzct.immediatechoice;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.fragment.UserFragment;
import com.qzct.immediatechoice.util.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PushActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView push_img_left;
    ImageView push_img_right;
    ImageView iv_push_go;
    String image_left_path = "未添加图片";
    String image_right_path = "未添加图片";
    Button bt_location;
    String locationDescribe;
    TextView location_hint;
    Handler handler;
    private User user = MyApplication.user;
    final int IMAGE_LEFT_UPLOAD = 0;
    final int IMAGE_RIGHT_UPLOAD = 1;
    final int GET_LOCATION_SUCCESS = 2;
    final int GET_LOCATION_FAILURE = 3;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GET_LOCATION_SUCCESS:
                        location_hint.setText(locationDescribe);
                        Log.i("locationDescribe", locationDescribe);
                        Toast.makeText(getApplicationContext(), "获取位置信息成功", Toast.LENGTH_LONG).show();
                        break;
                    case GET_LOCATION_FAILURE:
                        Toast.makeText(getApplicationContext(), "获取位置信息失败,请重新获取", Toast.LENGTH_LONG).show();

                        break;
                    default:

                        break;
                }

            }
        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_push_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushActivity.this.finish();
            }
        });

        push_img_left = (ImageView) findViewById(R.id.push_img_left);
        push_img_right = (ImageView) findViewById(R.id.push_img_right);
        iv_push_go = (ImageView) findViewById(R.id.iv_push_go);
        bt_location = (Button) findViewById(R.id.bt_location);
        location_hint = (TextView) findViewById(R.id.location_hint);
        push_img_left.setOnClickListener(this);
        push_img_right.setOnClickListener(this);
        iv_push_go.setOnClickListener(this);
        bt_location.setOnClickListener(this);


        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();


    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的


        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.i("BaiduLocationApiDem", sb.toString());
            locationDescribe = location.getLocationDescribe();
            Message msg = new Message();
            if (locationDescribe != null) {
                msg.obj = locationDescribe;
                msg.what = GET_LOCATION_SUCCESS;
                handler.sendMessage(msg);
            } else {
                msg.what = GET_LOCATION_FAILURE;
            }
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_LEFT_UPLOAD:

                image_left_path = getPathFromActivityResult(data);

                break;

            case IMAGE_RIGHT_UPLOAD:
                image_right_path = getPathFromActivityResult(data);
                break;


            default:
                break;

        }
    }

    public String getPathFromActivityResult(Intent data) {
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        if (data != null) {
            //            ContentResolver resolver = getContentResolver();
            Uri originalUri = data.getData();        //获得图片的uri
            String path = utils.getImageAbsolutePath(this, originalUri);
//            Toast.makeText(this, "Uri:" + originalUri + "path:" + path, Toast.LENGTH_LONG).show();
            return path;
        } else {
            return "";
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Question vote = new Question();
        switch (v.getId()) {
            case R.id.iv_push_go:
                EditText et_push_question_content = (EditText) findViewById(R.id.push_question_content);
                String question_content = null;
                question_content = et_push_question_content.getText().toString();
                String quizzer_name = user.getUsername();
                vote.setImage_left(image_left_path);
                vote.setImage_right(image_right_path);
                vote.setQuestion_content(question_content);
                vote.setQuizzer_name(quizzer_name);
                vote.setLocation(locationDescribe);
                if (!("".equals(question_content))) {
                    UploadTask uploadTask = new UploadTask(MyApplication.url_upload, vote);
                    uploadTask.execute();
                    PushActivity.this.finish();
                } else {
                    Toast.makeText(this, "您还没输入投票题目呢", Toast.LENGTH_SHORT).show();
                }


                break;


            case R.id.push_img_left:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, IMAGE_LEFT_UPLOAD);
                break;

            case R.id.push_img_right:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //              intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, IMAGE_RIGHT_UPLOAD);

                break;

            case R.id.bt_location:
                mLocationClient.start();

                break;


            default:
                break;

        }


    }


    class UploadTask extends AsyncTask<String, String, String> {
        String url;
        String image_left_path;
        String image_right_path;
        String question_content;
        String username;
        String locationDescribe;
        String quizzer_name;

        public UploadTask(String url, Question vote) {
            this.url = url;
            this.username = vote.getQuizzer_name();
            this.question_content = vote.getQuestion_content();
            this.image_left_path = vote.getImage_left();
            this.image_right_path = vote.getImage_right();
            this.locationDescribe = vote.getLocation();
            this.quizzer_name = vote.getQuizzer_name();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            //            String url = getString(R.string.url_image_text);
            HttpPost httpPost = new HttpPost(url);

            //                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            //                BasicNameValuePair pair1 = new BasicNameValuePair("username", username);
            //                BasicNameValuePair pair2 = new BasicNameValuePair("question_content", question_content);
            //                BasicNameValuePair pair3 = new BasicNameValuePair("image_left_path", image_left_path);
            //                BasicNameValuePair pair4 = new BasicNameValuePair("image_right_path", image_right_path);
            //                BasicNameValuePair pair5 = new BasicNameValuePair("locations", locationDescribe);
            //                parameters.add(pair1);
            //                parameters.add(pair2);
            //                parameters.add(pair3);
            //                parameters.add(pair4);
            //                parameters.add(pair5);
            try {
                Charset charset = Charset.forName("utf-8");
                MultipartEntity entity = new MultipartEntity();
                FileBody image_left = new FileBody(new File(image_left_path));
                FileBody image_right = new FileBody(new File(image_right_path));
                String image_left_name = image_left.getFilename();
                String image_right_name = image_right.getFilename();
                if (locationDescribe == null) {
                    locationDescribe = "未获得定位";
                }
                //                entity.addPart("username", new StringBody(username, charset));
                entity.addPart("question_content", new StringBody(question_content, charset));
                entity.addPart("image_left_name", new StringBody(image_left_name, charset));
                entity.addPart("image_right_name", new StringBody(image_right_name, charset));
                entity.addPart("quizzer_name", new StringBody(quizzer_name, charset));
                entity.addPart("quizzer_portrait", new StringBody(MyApplication.user.getPortrait_path(), charset));
                entity.addPart("locations", new StringBody(locationDescribe, charset));
                entity.addPart("image_left", image_left);
                entity.addPart("image_right", image_right);

                //                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                httpPost.setEntity(entity);


                HttpResponse hr = hc.execute(httpPost);
                if (hr.getStatusLine().getStatusCode() == 200) {
                    InputStream is = hr.getEntity().getContent();
                    String text = utils.getTextFromStream(is);
                    return text;
                } else {
                    return "2";
                }
            } catch (Exception e) {
                System.out.println("异常");
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String text) {
            if (text != null) {
                switch (text) {
                    case "0":
                        Toast.makeText(PushActivity.this, "发起投票失败", Toast.LENGTH_LONG).show();
                        break;

                    case "1":
                        Toast.makeText(PushActivity.this, "发起投票成功", Toast.LENGTH_LONG).show();
                        new UserFragment().getMyPush(); 
                        break;

                    case "2":
                        Toast.makeText(PushActivity.this, "连接网站失败", Toast.LENGTH_LONG).show();
                        break;


                    default:
                        break;
                }

            }

        }
    }


}