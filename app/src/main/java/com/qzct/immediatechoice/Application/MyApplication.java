package com.qzct.immediatechoice.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.qzct.immediatechoice.BuildConfig;
import com.qzct.immediatechoice.domain.User;
import com.qzct.immediatechoice.pager.ImageTextPager;
import com.qzct.immediatechoice.pager.VideoPager;

import org.xutils.x;

import java.io.File;

import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;


/**
 * Created by Qin on 2017-02-27.
 */

public class MyApplication extends Application {


    public static User user;
    public static ImageTextPager.ItemData imageTextItemData;
    public static VideoPager.ItemData videoItemData;

    @Override
    public void onCreate() {
        user = new User();
        super.onCreate();
        //初始化xUtils
        x.Ext.init(this);
        // 是否输出debug日志, 开启debug会影响性能.
        x.Ext.setDebug(BuildConfig.DEBUG);
        initSmallVideo(this);

    }

    public void updateUserPortrait(User user) {


    }

    public static void initSmallVideo(Context context) {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        VCamera.setDebugMode(true);
        VCamera.initialize(context);
    }

}
