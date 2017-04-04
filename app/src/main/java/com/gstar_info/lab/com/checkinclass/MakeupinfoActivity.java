package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.gstar_info.lab.com.checkinclass.clip.ClipHeaderActivity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FJS0420 on 2016/7/20.
 */

public class MakeupinfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private static final int RESULT_CAPTURE = 100;
    private static final int RESULT_PICK = 101;
    private static final int CROP_PHOTO = 102;
    public static final int ACADEMY_REQ = 103;
    public static final int SCHOOL_REQ = 104;
    public static final int ACADEMY_RES = 105;
    public static final int SCHOOL_RES = 106;
    public static final int NICK_RES = 107;
    public static final int NICK_REQ = 108;
    public static final int DISPLAY_RES = 109;
    public static final int DISPLAY_REQ = 110;
    @BindView(R.id.updateBaseInfoProgress)
    ProgressBar updateBaseInfoProgress;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.textView2)
    TextView textView2;
    private int HEADER_FALG = 107;


    private File tempFile;
    private Uri headerUri;

    ImageView iv_head_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeupinfo);
        AppManager.getAppManager().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
        }
        ButterKnife.bind(this);
        init();
        initData(savedInstanceState);
    }


    private void init() {
        iv_head_icon = (ImageView) findViewById(R.id.iv_headpic);
        iv_head_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseDialog();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/com.njtech/bigclass/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case RESULT_CAPTURE:
                if (resultCode == RESULT_OK) {
                    starCropPhoto(Uri.fromFile(tempFile));
                }
                break;
            case RESULT_PICK:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();

                    starCropPhoto(uri);
                }

                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {

                    if (intent != null) {
                        setPicToView(intent);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void showChooseDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                            startActivityForResult(intent, RESULT_CAPTURE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(Intent.createChooser(intent, "请选择图片"), RESULT_PICK);
                        }
                    }
                }).show();
    }

    /**
     * 打开截图界面
     *
     * @param uri 原图的Uri
     */
    public void starCropPhoto(Uri uri) {

        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipHeaderActivity.class);
        intent.setData(uri);
        intent.putExtra("side_length", 200);//裁剪图片宽高
        startActivityForResult(intent, CROP_PHOTO);

    }

    private void setPicToView(Intent picdata) {
        Uri uri = picdata.getData();

        if (uri == null) {
            return;
        }

        iv_head_icon.setImageURI(uri);
        headerUri = uri;

    }


    /**
     * @param dirPath
     * @return
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    public void updateBaseInfo() {
        if (headerUri != null) {
            updateHeader(headerUri);
        } else {
            updateHttp();
        }
    }


    public void updateHeader(final Uri uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = null;
                try {
                    token = HttpUtil.getToken("http://119.29.97.151//BigClass/token/getToken.php");//获取token接口
                    String headerPath = getRealFilePath(uri);
                    Log.i("json", token);
                    if (token != null) {
                        UploadManager uploadManager = new UploadManager();
                        String data = headerPath;
                        String key = null;
                        uploadManager.put(data, key, token,
                                new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                        try {
                                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res.get("key"));
                                            //上传后返回url
                                            String url = "http://onqif3xou.bkt.clouddn.com/" + res.get("key");
//                                        userInfo.setFace_img(url);
                                            Message message;
                                            message = handler.obtainMessage();
                                            message.obj = url;
                                            message.what = HEADER_FALG;
                                            message.sendToTarget();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, null);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MakeupinfoActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MakeupinfoActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HEADER_FALG) {
                textView2.setText((CharSequence) msg.obj);
                updateHttp();
            }
        }
    };


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param uri
     * @return the file path or null
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //更新个人信息
    public void updateHttp() {
//        if (userInfo.rightFlag()) {
//            Retrofit retrofit = HttpControl.getInstance().getRetrofit();
//            UserinfoService userinfoService = retrofit.create(UserinfoService.class);
//            userinfoService.updateBaseInfo(userInfo.getName(), userInfo.getGrade(), userInfo.getSignature(), userInfo.getSex(), userInfo.getFace_img(), userInfo.getSchool_id(), userInfo.getAcademy_id())
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<RegisterEntity>() {
//                        @Override
//                        public void onCompleted() {
//                            Toast.makeText(MakeupinfoActivity.this, "信息更新成功,请重新登录", Toast.LENGTH_SHORT).show();
//                            updateBaseInfoProgress.setVisibility(View.GONE);
//                            MyApplication.restartApp();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Toast.makeText(MakeupinfoActivity.this, "更新信息失败", Toast.LENGTH_SHORT).show();
//                            updateBaseInfoProgress.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onNext(RegisterEntity registerEntity) {
//                            if (registerEntity.getStatus() != 1) {
//                                onError(new Exception());
//                            }
//                        }
//                    });
//        } else {
//            updateBaseInfoProgress.setVisibility(View.GONE);
//        }
    }


    @OnClick(R.id.button)
    public void onClick() {
        updateBaseInfo();
    }
}
