package com.qzct.immediatechoice.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.nanchen.compresshelper.CompressHelper;
import com.qzct.immediatechoice.Application.MyApplication;
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.PathUtils;
import com.qzct.immediatechoice.util.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;


/**
 * 发起投票
 */
public class PushActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView push_left;
    private ImageView push_right;
    private ImageView iv_push_go;
    private String left_path = "Nothing";
    private String right_path = "Nothing";
    //    private ImageView push_video_left;
//    private ImageView push_video_right;
    private String quizzer_name;
    private String question_content;
    private Button bt_location;
    private String locationDescribe;
    private TextView location_hint;
    private Handler handler;
    private User user = MyApplication.user;
    final int IMAGE_LEFT_UPLOAD = 0;
    final int IMAGE_RIGHT_UPLOAD = 1;
    final int GET_LOCATION_SUCCESS = 2;
    final int GET_LOCATION_FAILURE = 3;
    final int VIDEO_LEFT_UPLOAD = 4;
    final int VIDEO_RIGHT_UPLOAD = 5;
    final int CHOICE_GROUP = 6;
    final private String url = Config.url_upload;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private ImageView iv_back;
    private boolean isUploadImage = false;
    private EditText et_push_question_content;
    private Button choice_group;
    private JSONArray push_group_ids;
    private boolean isImage;
    private int group_id;
    private File flle_left;
    private File file_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        isImage = getIntent().getBooleanExtra("isImage", true);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GET_LOCATION_SUCCESS:
//                        location_hint.setText(locationDescribe);
//                        Toast.makeText(getApplicationContext(), "获取位置信息成功", Toast.LENGTH_LONG).show();
                        break;
                    case GET_LOCATION_FAILURE:
                        Toast.makeText(getApplicationContext(), "获取位置信息失败,请重新获取", Toast.LENGTH_LONG).show();

                        break;
                    default:
                        break;
                }

            }
        };


        initView();
        initData();


    }

    private void initData() {
//        initRecorder();
        //设置点击监听
        iv_back.setOnClickListener(this);
        push_left.setOnClickListener(this);
        push_right.setOnClickListener(this);
//        push_video_left.setOnClickListener(this);
//        push_video_right.setOnClickListener(this);
        iv_push_go.setOnClickListener(this);
        bt_location.setOnClickListener(this);
        choice_group.setOnClickListener(this);
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();

    }


    private void initView() {
        //获得View
        iv_back = (ImageView) findViewById(R.id.iv_push_back);
        push_left = (ImageView) findViewById(R.id.push_left);
        push_right = (ImageView) findViewById(R.id.push_right);
        if (!isImage) {
            push_left.setImageResource(R.mipmap.push_video);
            push_right.setImageResource(R.mipmap.push_video);
        }
//        push_video_left = (ImageView) findViewById(R.id.push_video_left);
//        push_video_right = (ImageView) findViewById(R.id.push_video_right);
        iv_push_go = (ImageView) findViewById(R.id.iv_push_go);
        choice_group = (Button) findViewById(R.id.choice_group);
        bt_location = (Button) findViewById(R.id.bt_location);
        location_hint = (TextView) findViewById(R.id.location_hint);
        et_push_question_content = (EditText) findViewById(R.id.push_question_content);


    }


    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        //初始化一个投票类
        Question vote = new Question();
        switch (v.getId()) {
            //发起
            case R.id.iv_push_go:
                quizzer_name = user.getUsername();
                question_content = et_push_question_content.getText().toString();
                //判断content是否有数据
                if (!("".equals(question_content))) {
                    //上传
                    uplooad();
                    PushActivity.this.finish();
                } else {
                    Toast.makeText(this, "您还没输入投票题目呢", Toast.LENGTH_SHORT).show();
                }
                break;
            //返回
            case R.id.iv_push_back:
                PushActivity.this.finish();
                break;

            //选择左图
            case R.id.push_left:
                //选择左图
                if (isImage) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_LEFT_UPLOAD);
                } else {
                    //选择左视频
//                    intent = new Intent(this, MediaRecorderActivity.class);
//                    intent.putExtra(OVER_ACTIVITY_NAME, PushActivity.class.getName());
//                    intent.putExtra(MEDIA_RECORDER_CONFIG_KEY, config);
                    intent = new Intent(PushActivity.this, YWRecordVideoActivity.class);
                    startActivityForResult(intent, VIDEO_LEFT_UPLOAD);
                }
                break;
            //选择右图
            case R.id.push_right:
                //选择右图
                if (isImage) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_RIGHT_UPLOAD);
                } else {
                    //选择右视频
//                    intent = new Intent(this, MediaRecorderActivity.class);
//                    intent.putExtra(OVER_ACTIVITY_NAME, PushActivity.class.getName());
//                    intent.putExtra(MEDIA_RECORDER_CONFIG_KEY, config);
                    intent = new Intent(PushActivity.this, YWRecordVideoActivity.class);
                    startActivityForResult(intent, VIDEO_RIGHT_UPLOAD);
                }

                break;

            //选择左视频
//            case R.id.push_video_left:
////
////                intent = new Intent(this, MediaRecorderActivity.class);
////                intent.putExtra(OVER_ACTIVITY_NAME, PushActivity.class.getName());
////                intent.putExtra(MEDIA_RECORDER_CONFIG_KEY, config);
////                startActivityForResult(intent, VIDEO_LEFT_UPLOAD);
//                break;
            //选择右视频
//            case R.id.push_video_right:
//
//                break;
            //选择可见群组
            case R.id.choice_group:
                Intent group = new Intent(PushActivity.this, ChoiceGroupActivity.class);
                startActivityForResult(group, CHOICE_GROUP);
                break;
            //获取定位
            case R.id.bt_location:
                mLocationClient.start();

                break;
            default:
                break;

        }


    }

    /**
     * 从其他Activity返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                //左图上传
                case IMAGE_LEFT_UPLOAD:
                    DisposeResultImage(IMAGE_LEFT_UPLOAD, data);
                    break;
                //右图上传
                case IMAGE_RIGHT_UPLOAD:
                    DisposeResultImage(IMAGE_RIGHT_UPLOAD, data);

                    break;
                //左视频上传
                case VIDEO_LEFT_UPLOAD:
                    DisposeResultVideo(VIDEO_LEFT_UPLOAD, data);

                    break;
                //右视频上传
                case VIDEO_RIGHT_UPLOAD:
                    DisposeResultVideo(VIDEO_RIGHT_UPLOAD, data);

                    break;
                //选择群组上传
                case CHOICE_GROUP:
//                    List<String> groupIdList = data.getStringArrayListExtra("groupIdList");
                    group_id = data.getIntExtra("group_id", 0);
//                    push_group_ids = new JSONArray();
//                    for (String group_id : groupIdList) {
//                        try {
//                            push_group_ids.put(new JSONObject().put("group_id", group_id));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    break;
                default:
                    break;

            }
        }
    }

    /**
     * 处理返回的图片
     *
     * @param what
     * @param data
     */
    private void DisposeResultImage(int what, Intent data) {
        switch (what) {
            case IMAGE_LEFT_UPLOAD:
                left_path = PathUtils.getPathFromActivityResult(this, data);
                flle_left = CompressHelper.getDefault(this).compressToFile(new File(left_path));
                if (left_path != null) {
                    push_left.setPadding(0, 0, 0, 0);
                    push_left.setBackgroundColor(Color.TRANSPARENT);
                    Bitmap bitmap = BitmapFactory.decodeFile(left_path);
                    push_left.setImageBitmap(bitmap);
                }

                break;
            case IMAGE_RIGHT_UPLOAD:
                right_path = PathUtils.getPathFromActivityResult(this, data);
                file_right = CompressHelper.getDefault(this).compressToFile(new File(right_path));
                if (right_path != null) {
                    push_right.setPadding(0, 0, 0, 0);
                    push_right.setBackgroundColor(Color.TRANSPARENT);
                    Bitmap bitmap = BitmapFactory.decodeFile(right_path);
                    push_right.setImageBitmap(bitmap);
                }

                break;
        }
        isUploadImage = true;
    }

    /**
     * 处理返回的视频
     *
     * @param what
     * @param data
     */

    String videoScreenshot;

    private void DisposeResultVideo(int what, Intent data) {
        String path = data.getStringExtra("videoPath");
        videoScreenshot = data.getStringExtra("framePicPath");
        if (videoScreenshot != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);
            switch (what) {
                case VIDEO_LEFT_UPLOAD:
                    push_left.setPadding(0, 0, 0, 0);
                    push_left.setImageBitmap(bitmap);
                    left_path = path;
                    flle_left = new File(left_path);

                    break;
                case VIDEO_RIGHT_UPLOAD:
                    push_right.setPadding(0, 0, 0, 0);
                    push_right.setImageBitmap(bitmap);
                    right_path = path;
                    file_right = new File(right_path);


                    break;
            }
        }

        isUploadImage = false;

    }


    /**
     * 异步上传
     */
    private void uplooad() {
        RequestParams entity = new RequestParams(url);
        String type = "video";
        if (isUploadImage) {
            type = "image";
        }
        Question question = new Question(0, group_id, question_content,
                utils.getNetUrlFormLocalPath(left_path, type),
                utils.getNetUrlFormLocalPath(right_path, type),
                quizzer_name, user.getPortrait_path(), 0, 0, null,
                locationDescribe, null
        );
        JSONObject jsonObject = question.getJSONObject();
        entity.addBodyParameter("question", jsonObject.toString());
//        entity.addBodyParameter("group_ids", push_group_ids.toString());
        entity.addBodyParameter("msg", type);
        entity.addBodyParameter("file_left", flle_left);
        entity.addBodyParameter("file_right", file_right);
//        entity.addBodyParameter("file_left", new File(left_path));
//        entity.addBodyParameter("file_right", new File(right_path));
        Log.d("qin", "x.http");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    if (result.equals("1")) {
                        Toast.makeText(PushActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PushActivity.this, "发布失败,请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PushActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 定位初始化
     */
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

    /**
     * 定位监听
     */
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

        //        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }


}