package com.qzct.immediatechoice.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.qzct.immediatechoice.R;
import com.qzct.immediatechoice.application.MyApplication;
import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.QuestionVideo;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.fragment.UserFragment;
import com.qzct.immediatechoice.util.Config;
import com.qzct.immediatechoice.util.PathUtils;
import com.qzct.immediatechoice.util.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.AutoVBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;

import static mabeijianxi.camera.MediaRecorderActivity.MEDIA_RECORDER_CONFIG_KEY;
import static mabeijianxi.camera.MediaRecorderActivity.OVER_ACTIVITY_NAME;


/**
 * 发起投票
 */
public class PushActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView push_img_left;
    private ImageView push_img_right;
    private ImageView iv_push_go;
    private String image_left_path = "Nothing";
    private String image_right_path = "Nothing";
    private String video_left_url;
    private String video_right_url;
    private ImageView push_video_left;
    private ImageView push_video_right;
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
    final int UPLOAD_IMAGE = 6;
    final int UPLOAD_VIDEO = 7;
    final private String url = Config.url_upload;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private ImageView iv_back;
    private MediaRecorderConfig config;
    private boolean isUploadImage = false;
    private boolean isUploadVideo = false;
    private EditText et_push_question_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GET_LOCATION_SUCCESS:
                        location_hint.setText(locationDescribe);
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


        initView();
        initData();


    }

    private void initData() {
        initRecorder();
        //设置点击监听
        iv_back.setOnClickListener(this);
        push_img_left.setOnClickListener(this);
        push_img_right.setOnClickListener(this);
        push_video_left.setOnClickListener(this);
        push_video_right.setOnClickListener(this);
        iv_push_go.setOnClickListener(this);
        bt_location.setOnClickListener(this);

        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();

    }

    private void initRecorder() {

        config = new MediaRecorderConfig.Buidler()
                .doH264Compress(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                )
                .setMediaBitrateConfig(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                )
                .smallVideoWidth(480)
                .smallVideoHeight(360)
                .recordTimeMax(6 * 1000)
                .maxFrameRate(20)
                .minFrameRate(8)
                .captureThumbnailsTime(1)
                .recordTimeMin((int) (1.5 * 1000))
                .build();
    }

    private void initView() {
        //获得View
        iv_back = (ImageView) findViewById(R.id.iv_push_back);
        push_img_left = (ImageView) findViewById(R.id.push_img_left);
        push_img_right = (ImageView) findViewById(R.id.push_img_right);
        push_video_left = (ImageView) findViewById(R.id.push_video_left);
        push_video_right = (ImageView) findViewById(R.id.push_video_right);
        iv_push_go = (ImageView) findViewById(R.id.iv_push_go);
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
        Intent intent = null;
        //初始化一个投票类
        Question vote = new Question();
        switch (v.getId()) {
            //发起
            case R.id.iv_push_go:
                quizzer_name = user.getUsername();
                question_content = et_push_question_content.getText().toString();
                //判断上传图片
                if (isUploadImage == true) {

                    vote.setImage_left(image_left_path);
                    vote.setImage_right(image_right_path);
                    vote.setQuestion_content(question_content);
                    vote.setQuizzer_name(quizzer_name);
                    vote.setLocation(locationDescribe);
                    //判断content是否有数据
                    if (!("".equals(question_content))) {
                        UploadTask uploadTask = new UploadTask(Config.url_upload, vote);
                        uploadTask.execute();
                        PushActivity.this.finish();
                    } else {
                        Toast.makeText(this, "您还没输入投票题目呢", Toast.LENGTH_SHORT).show();
                    }
                    //判断上传视频
                } else if (isUploadVideo == true) {
                    Log.d("qin", "isUploadVideo");
                    uplooadVideo();
                    PushActivity.this.finish();
                }


                break;
            //返回
            case R.id.iv_push_back:
                PushActivity.this.finish();
                break;

            //选择左图
            case R.id.push_img_left:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_LEFT_UPLOAD);
                break;
            //选择右图
            case R.id.push_img_right:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //              intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, IMAGE_RIGHT_UPLOAD);

                break;

            //选择左视频
            case R.id.push_video_left:

//                MediaRecorderActivity.goSmallVideoRecorder(this, PushActivity.class.getName(), config);
                intent = new Intent(this, MediaRecorderActivity.class);
                intent.putExtra(OVER_ACTIVITY_NAME, PushActivity.class.getName());
                intent.putExtra(MEDIA_RECORDER_CONFIG_KEY, config);
                startActivityForResult(intent, VIDEO_LEFT_UPLOAD);

//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("video/*");
//                startActivityForResult(intent, VIDEO_LEFT_UPLOAD);
                break;
            //选择右视频
            case R.id.push_video_right:
                intent = new Intent(this, MediaRecorderActivity.class);
                intent.putExtra(OVER_ACTIVITY_NAME, PushActivity.class.getName());
                intent.putExtra(MEDIA_RECORDER_CONFIG_KEY, config);
                startActivityForResult(intent, VIDEO_RIGHT_UPLOAD);
//                intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("video/*");
//                //              intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent, VIDEO_RIGHT_UPLOAD);

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
     * 上传视频
     */
    private void uplooadVideo() {
        RequestParams entity = new RequestParams(url);
        QuestionVideo questionVideo = new QuestionVideo(
                question_content, getNetUrlFormLocalPath(video_left_url, "video"),
                getNetUrlFormLocalPath(video_right_url, "Video"), quizzer_name, user.getPortrait_path(),
                locationDescribe
        );
        JSONObject jsonObject = questionVideo.getJSONObject();
        entity.addBodyParameter("question_video", jsonObject.toString());
        entity.addBodyParameter("msg", UPLOAD_VIDEO + "");
        entity.addBodyParameter("video_left", new File(video_left_url));
        entity.addBodyParameter("video_right", new File(video_right_url));
        Log.d("qin", "x.http");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    if (result.equals("1")) {
                        Toast.makeText(PushActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PushActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
     * 获取对应的网络地址
     *
     * @param localPath
     * @return
     */
    private String getNetUrlFormLocalPath(String localPath, String type) {
        if (type == "image") {
            return Config.server_video_url + getFileName(localPath);
        } else if (type == "video") {
            return Config.server_img_url + getFileName(localPath);
        } else {
            return localPath;
        }
    }

    /**
     * 获取文件名（有拓展名）
     *
     * @param Path
     * @return
     */
    private String getFileName(String Path) {
        int index = Path.lastIndexOf("/");
        if (index > 0) {
            Path = Path.substring(index + 1);
        }
        return Path;
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

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 从其他Activity返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    Uri videoUri;
    String videoScreenshot;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            default:
                break;

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
                image_left_path = PathUtils.getPathFromActivityResult(this, data);
                if (image_left_path != null) {
                    push_img_left.setPadding(0, 0, 0, 0);
                    push_img_left.setBackgroundColor(Color.TRANSPARENT);
                    Bitmap bitmap = BitmapFactory.decodeFile(image_left_path);
                    push_img_left.setImageBitmap(bitmap);
                }

                break;
            case IMAGE_RIGHT_UPLOAD:
                image_right_path = PathUtils.getPathFromActivityResult(this, data);
                if (image_right_path != null) {
                    push_img_right.setPadding(0, 0, 0, 0);
                    push_img_right.setBackgroundColor(Color.TRANSPARENT);
                    Bitmap bitmap = BitmapFactory.decodeFile(image_left_path);
                    push_img_right.setImageBitmap(bitmap);
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
    private void DisposeResultVideo(int what, Intent data) {
        videoUri = Uri.parse(data.getStringExtra(MediaRecorderActivity.VIDEO_URI));
        videoScreenshot = data.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);

        if (videoScreenshot != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);
            switch (what) {
                case VIDEO_LEFT_UPLOAD:
                    push_video_left.setPadding(0, 0, 0, 0);
                    push_video_left.setImageBitmap(bitmap);
                    video_left_url = videoUri.toString();
//                    video_left_url = PathUtils.getPath(this, videoUri);


                    break;
                case VIDEO_RIGHT_UPLOAD:
                    push_video_right.setPadding(0, 0, 0, 0);
                    push_video_right.setImageBitmap(bitmap);
                    video_right_url = videoUri.toString();
//                    video_right_url = PathUtils.getPath(this, videoUri);


                    break;
            }
        }

        isUploadVideo = true;

    }


    /**
     * 异步上传
     */
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

        //后台上传
        @Override
        protected String doInBackground(String... params) {

            HttpClient hc = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            try {
                Charset charset = Charset.forName("utf-8");
                MultipartEntity entity = new MultipartEntity();
                FileBody image_left = new FileBody(new File(image_left_path));
                FileBody image_right = new FileBody(new File(image_right_path));
                String image_left_name = image_left.getFilename();
                String image_right_name = image_right.getFilename();
                //判断是否获取定位
                if (locationDescribe == null) {
                    locationDescribe = "未获得定位";
                }
                entity.addPart("msg", new StringBody(UPLOAD_IMAGE + "", charset));
                entity.addPart("question_content", new StringBody(question_content, charset));
                entity.addPart("image_left_name", new StringBody(image_left_name, charset));
                entity.addPart("image_right_name", new StringBody(image_right_name, charset));
                entity.addPart("quizzer_name", new StringBody(quizzer_name, charset));
                entity.addPart("quizzer_portrait", new StringBody(MyApplication.user.getPortrait_path(), charset));
                entity.addPart("locations", new StringBody(locationDescribe, charset));
                entity.addPart("image_left", image_left);
                entity.addPart("image_right", image_right);

                httpPost.setEntity(entity);
                HttpResponse hr = hc.execute(httpPost);
                //返回码
                if (hr.getStatusLine().getStatusCode() == 200) {
                    InputStream is = hr.getEntity().getContent();
                    String result = utils.getTextFromStream(is);
                    return result;
                } else {
                    return "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //判断result是否存在
            if (result != null) {
                switch (result) {
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