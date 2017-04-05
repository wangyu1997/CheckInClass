package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.clip.ClipHeaderActivity;
import com.gstar_info.lab.com.checkinclass.model.ArrayEntity;
import com.gstar_info.lab.com.checkinclass.model.LoginEntity;
import com.gstar_info.lab.com.checkinclass.model.StudentInfoBean;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.BitmapUtil;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;
import com.gstar_info.lab.com.checkinclass.utils.HttpUtil;
import com.gstar_info.lab.com.checkinclass.utils.ImageFilter;
import com.gstar_info.lab.com.checkinclass.utils.MyApplication;
import com.gstar_info.lab.com.checkinclass.utils.UserInsertHelper;
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
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

/**
 * Created by FJS0420 on 2016/7/20.
 */

public class upHeaderActivity extends AppCompatActivity {

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
    @BindView(R.id.header_main)
    RelativeLayout headerMain;
    @BindView(R.id.button2)
    Button button2;
    private int HEADER_FALG = 107;


    private File tempFile;
    private Uri headerUri;
    private String headUrl;

    ImageView iv_head_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
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
                MyApplication.restartApp();
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
        final Uri uri = picdata.getData();

        if (uri == null) {
            return;
        }

        iv_head_icon.setImageURI(uri);
        headerUri = uri;

        //异步处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                //高斯模糊处理图片
                Bitmap bitmap = BitmapUtil.getBitmapFromUri(uri);
                bitmap = ImageFilter.doBlur(bitmap, 5, false);
                final BitmapDrawable bd = new BitmapDrawable(bitmap);
                //处理完成后返回主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        headerMain.setBackground(bd);
                    }
                });
            }
        }).start();

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
        Toast.makeText(upHeaderActivity.this, "正在上传头像，请稍后...", Toast.LENGTH_SHORT).show();
        updateBaseInfoProgress.setVisibility(View.VISIBLE);
        updateHeader(headerUri);
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
                                Toast.makeText(upHeaderActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                                updateBaseInfoProgress.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(upHeaderActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                            updateBaseInfoProgress.setVisibility(View.GONE);
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
                headUrl = (String) msg.obj;
                upHead(headUrl);
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

    public void upHead(String head) {
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.upHeader(head)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayEntity>() {
                    @Override
                    public void onCompleted() {
                        updateBaseInfoProgress.setVisibility(View.GONE);
                        Toast.makeText(upHeaderActivity.this, "上传头像成功，正在跳转至首页...", Toast.LENGTH_SHORT).show();
                        MyApplication.restartApp();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(upHeaderActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                        updateBaseInfoProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ArrayEntity arrayEntity) {
                        if (arrayEntity.isError()) {
                            Toast.makeText(upHeaderActivity.this, "上传头像失败:" + arrayEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            StudentInfoBean dataBean = UserInsertHelper.getUserInfo(upHeaderActivity.this);
                            dataBean.setHeader(headUrl);
                            UserInsertHelper.updateUser(upHeaderActivity.this, dataBean);
                        }
                    }
                });
    }


    @OnClick(R.id.button2)
    public void onClick() {
        if (headerUri != null) {
            updateBaseInfo();
        } else {
            Toast.makeText(upHeaderActivity.this, "请先上传头像...", Toast.LENGTH_SHORT).show();
        }
    }
}
