package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.model.LoginEntity;
import com.gstar_info.lab.com.checkinclass.model.StudentInfoBean;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;
import com.gstar_info.lab.com.checkinclass.utils.MyApplication;
import com.gstar_info.lab.com.checkinclass.utils.UserInsertHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int INSERTUSER = 0x0001;
    @BindView(R.id.edit_user)
    EditText mEditUser;
    @BindView(R.id.edit_passwd)
    EditText mEditPasswd;
    @BindView(R.id.tv_forget)
    TextView mTvForget;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar;
    private LoginEntity.DataBean mStudentInfoBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        AppManager.getAppManager().addActivity(this);

        init();

    }

    private void init() {

        if (UserInsertHelper.getUserInfo(LoginActivity.this) != null) {
            StudentInfoBean bean = UserInsertHelper.getUserInfo(this);
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            intent.putExtra("aid", bean.getAid());
            intent.putExtra("aname", bean.getA_name());
            startActivity(intent);
            finish();
        }
    }


    private void login() {
        mProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "正在登录，请稍后", Toast.LENGTH_SHORT).show();
        final String username = mEditUser.getText().toString();
        final String passwd = mEditPasswd.getText().toString();
        final String deviceId = MyApplication.getDeviceId();

        Log.d(TAG, "login: " + username + "|" + passwd + "|" + deviceId);
        API API = HttpControl.getInstance()
                .getRetrofit()
                .create(API.class);
        API.studentLogin(username, passwd, deviceId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginEntity>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "login Error!");
                        Toast.makeText(LoginActivity.this,
                                "用户名或密码错误", Toast.LENGTH_SHORT)
                                .show();
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(LoginEntity loginEntity) {
                        if (!loginEntity.isError()) {
                            Toast.makeText(LoginActivity.this, "登录成功:", Toast.LENGTH_SHORT).show();
                            mStudentInfoBean = loginEntity.getData();
                            UserInsertHelper.removeUser(LoginActivity.this);
                            UserInsertHelper.insertUser(LoginActivity.this, mStudentInfoBean, passwd);
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            intent.putExtra("aid", loginEntity.getData().getAid());
                            intent.putExtra("aname", loginEntity.getData().getA_name());
                            intent.putExtra("passwd", passwd);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败:" + loginEntity.getMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.tv_forget, R.id.btn_login, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPassActivity.class));
                break;
            case R.id.btn_login:
                if (mEditUser.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEditPasswd.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
                break;
            case R.id.btn_regist:
                startActivity(new Intent(this, Regist1Activity.class));
                break;
        }
    }
}
