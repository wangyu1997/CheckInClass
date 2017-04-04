package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.model.LoginEntity;
import com.gstar_info.lab.com.checkinclass.model.RegistVerEntity;
import com.gstar_info.lab.com.checkinclass.model.RegisterEntity;
import com.gstar_info.lab.com.checkinclass.model.StringEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;
import com.gstar_info.lab.com.checkinclass.utils.MyApplication;
import com.gstar_info.lab.com.checkinclass.utils.UserInsertHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;


/**
 * Created by ritchiehuang on 02/04/2017.
 */

public class Regist2Activity extends AppCompatActivity {

    private static final String TAG = "Regist2Activity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_email)
    EditText mEditEmail;
    @BindView(R.id.edit_verifycode)
    EditText mEditVerifycode;
    @BindView(R.id.btn_resend)
    Button mBtnResend;
    @BindView(R.id.edit_passwd)
    EditText mEditPasswd;
    @BindView(R.id.edit_passwdconfirm)
    EditText mEditPasswdconfirm;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private Intent mIntent;
    private RegistVerEntity mRegistVerEntity;
    private String rawcode;
    private String code;
    private int i = 0x003;
    private String major_name;
    private int mid;
    private String classinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);

        init();


    }

    private void init() {
        mIntent = getIntent();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        major_name = mIntent.getStringExtra("major_name");
        classinfo = mIntent.getStringExtra("classinfo");
        mid = mIntent.getIntExtra("mid", -1);
        mRegistVerEntity = (RegistVerEntity) mIntent.getBundleExtra("bundle").getSerializable("regist");


    }


    private void regist() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(Regist2Activity.this,
                "正在注册，请稍后....", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);

        final String deviceId = MyApplication.getDeviceId();

        api.studentRegister(mRegistVerEntity.getUsername(),
                mRegistVerEntity.getPassword(),
                mRegistVerEntity.getMid(),
                mRegistVerEntity.getName(),
                mRegistVerEntity.getSex(),
                deviceId,
                mRegistVerEntity.getClassinfo())
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Regist2Activity.this,
                                "注册失败", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(RegisterEntity registerEntity) {
                        if (registerEntity.isError()) {
                            Toast.makeText(Regist2Activity.this,
                                    "注册失败", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(Regist2Activity.this,
                                    "注册成功,正在登录请稍后", Toast.LENGTH_SHORT).show();
                            login();
                        }
                    }
                });

    }

    private void login() {
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        final String deviceId = MyApplication.getDeviceId();

        api.studentLogin(mRegistVerEntity.getUsername(), mRegistVerEntity.getPassword(),
                deviceId)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginEntity>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Regist2Activity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(LoginEntity loginEntity) {
                        if (loginEntity.isError()) {
                            Toast.makeText(Regist2Activity.this, "登录失败:" + loginEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Regist2Activity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            UserInsertHelper.insertUser(Regist2Activity.this, loginEntity.getData());
                            startActivity(new Intent(Regist2Activity.this, upHeaderActivity.class));
                        }
                    }
                });

    }

    private void send() {
        mProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(Regist2Activity.this, "正在发送验证码，请稍后", Toast.LENGTH_SHORT).show();
        String email = mEditEmail.getText().toString();
        if (!checkEmaile(email)) {
            Toast.makeText(MyApplication.getGlobalContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.getcode("send", email)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StringEntity>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                        mBtnResend.setClickable(false);
                        mBtnResend.setText(i + "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; i > 0; i--) {
                                    handler.sendEmptyMessage(-9);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-8);
                            }
                        }).start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Regist2Activity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(StringEntity stringEntity) {
                        if (stringEntity.isError()) {
                            Toast.makeText(Regist2Activity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                        } else {
                            rawcode = stringEntity.getData();
                            Toast.makeText(Regist2Activity.this, "验证码发送成功，请回邮箱查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    /**
     *
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mBtnResend.setText(i + "");
            } else if (msg.what == -8) {
                mBtnResend.setText("发送验证码");
                mBtnResend.setClickable(true);
                i = 60;
            }
        }

    };

    /**
     * 正则表达式校验邮箱
     *
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    private static boolean checkEmaile(String emaile) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }


    @OnClick({R.id.btn_resend, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_resend:
                send();
                break;
            case R.id.btn_regist:
                code = mEditVerifycode.getText().toString();
                if (!code.equals(rawcode)) {
                    Toast.makeText(MyApplication.getGlobalContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                mRegistVerEntity.setEmail(mEditEmail.getText().toString());
                mRegistVerEntity.setPassword(mEditPasswd.getText().toString());
                mRegistVerEntity.setPasswordCom(mEditPasswd.getText().toString());
                if (mRegistVerEntity.isLeagel2()) {
                    regist();
                }
                break;
        }
    }
}
