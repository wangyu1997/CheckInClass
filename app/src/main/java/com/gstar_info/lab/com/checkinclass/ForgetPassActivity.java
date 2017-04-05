package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
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
import com.gstar_info.lab.com.checkinclass.model.ArrayEntity;
import com.gstar_info.lab.com.checkinclass.model.StringEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;
import com.gstar_info.lab.com.checkinclass.utils.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

public class ForgetPassActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_email)
    EditText mEditEmail;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.edit_passwd)
    EditText mEditPasswd;
    @BindView(R.id.edit_passwdconfirm)
    EditText mEditPasswdconfirm;
    @BindView(R.id.btn_change)
    Button mBtnChange;
    @BindView(R.id.progressBar3)
    ProgressBar mProgressBar3;

    private int i = 60;
    private String rawcode;
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgetpass);
        ButterKnife.bind(this);
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

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @OnClick({R.id.btn_send, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                send();

                break;
            case R.id.btn_change:

                change();
                break;
        }
    }

    private void change() {
        code = mEditCode.getText().toString();
        if (!code.equals(rawcode)) {
            Toast.makeText(MyApplication.getGlobalContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = mEditEmail.getText().toString();
        String password = mEditPasswd.getText().toString();
        String passwordCom = mEditPasswdconfirm.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkEmaile(email)) {
            Toast.makeText(MyApplication.getGlobalContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordCom.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordCom.equals(password)) {
            Toast.makeText(MyApplication.getGlobalContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(MyApplication.getGlobalContext(), "密码必须超过8位", Toast.LENGTH_SHORT).show();
            return;
        }
        postChange(email, password);

    }

    public void postChange(String email, String password) {
        mProgressBar3.setVisibility(View.VISIBLE);
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.forgetPass(email, password)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayEntity>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar3.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ForgetPassActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                        mProgressBar3.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ArrayEntity arrayEntity) {
                        if (arrayEntity.isError()) {
                            Toast.makeText(ForgetPassActivity.this, "修改密码失败:" + arrayEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPassActivity.this, "修改密码成功，请重新登录", Toast.LENGTH_SHORT).show();
                            MyApplication.restartApp();
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
                mBtnSend.setText(i + "");
            } else if (msg.what == -8) {
                mBtnSend.setText("发送验证码");
                mBtnSend.setClickable(true);
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

    private void send() {
        mProgressBar3.setVisibility(View.VISIBLE);
        String email = mEditEmail.getText().toString();
        if (!checkEmaile(email)) {
            Toast.makeText(MyApplication.getGlobalContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            mProgressBar3.setVisibility(View.GONE);
            return;
        }
        Toast.makeText(ForgetPassActivity.this, "正在发送验证码，请稍后", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.resetPass("reset", email, "student")
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StringEntity>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar3.setVisibility(View.GONE);
                        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                        mBtnSend.setClickable(false);
                        mBtnSend.setText(i + "");
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
                        Toast.makeText(ForgetPassActivity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                        mProgressBar3.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(StringEntity stringEntity) {
                        if (stringEntity.isError()) {
                            Toast.makeText(ForgetPassActivity.this, "发送验证码失败:" + stringEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            rawcode = stringEntity.getData();
                            Toast.makeText(ForgetPassActivity.this, "验证码发送成功，请回邮箱查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
