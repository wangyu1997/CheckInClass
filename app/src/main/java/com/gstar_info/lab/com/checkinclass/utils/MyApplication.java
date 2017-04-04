package com.gstar_info.lab.com.checkinclass.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.qiniu.android.storage.Configuration;
import com.gstar_info.lab.com.checkinclass.utils.cache.SetCookieCache;
import com.gstar_info.lab.com.checkinclass.utils.persistence.SharedPrefsCookiePersistor;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.UploadManager;

import java.util.UUID;

/**
 * Created by ritchiehuang on 4/1/17.
 */

public class MyApplication extends Application {
    static Context sContext;
    static String deviceId;
    static int screenwidth = 1080;
    private static String token = "";
    static ClearableCookieJar sCookieJar;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
                .recorder(null)  // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, null)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);

    }


    public static void restartApp() {
        AppManager.getAppManager().AppExit(getGlobalContext());
        Intent i = getGlobalContext().getPackageManager()
                .getLaunchIntentForPackage(getGlobalContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getGlobalContext().startActivity(i);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static Context getGlobalContext() {
        return sContext;
    }

    public static ClearableCookieJar getCookieJar() {
        if (sCookieJar == null) {
            return new PersistentCookieJar(new SetCookieCache(),
                    new SharedPrefsCookiePersistor(sContext));
        } else {
            return sCookieJar;
        }
    }

    public static int getWidth() {
        return screenwidth;
    }

    public static void setWidth(int width) {
        screenwidth = width;
    }

    public static String getDeviceId() {
        return new DeviceUuidFactory(sContext).getDeviceUuid().toString();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("login", MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }
}
