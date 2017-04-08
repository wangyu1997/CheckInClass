package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.PopUpWindow.Action_PopUp;
import com.gstar_info.lab.com.checkinclass.PopUpWindow.Comfirm_PopUp;
import com.gstar_info.lab.com.checkinclass.model.ArrayEntity;
import com.gstar_info.lab.com.checkinclass.model.CourseinfoEntity;
import com.gstar_info.lab.com.checkinclass.model.ObjEntity;
import com.gstar_info.lab.com.checkinclass.model.SignHistoryEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

public class CourseDetailActivity extends AppCompatActivity {


    private static final String TAG = "CourseDetailActivity";
    private static final int REFRESHVIEW = 0x005;
    private CourseinfoEntity.DataBean bean;

    @BindView(R.id.tv_coursename)
    TextView mTvCoursename;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.teacher_head)
    SimpleDraweeView mTeacherHead;
    @BindView(R.id.teacher_name)
    TextView mTeacherName;
    @BindView(R.id.head_layout)
    LinearLayout mHeadLayout;
    @BindView(R.id.tv_wifi)
    TextView mTvWifi;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_create)
    TextView mTvCreate;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.join_layout)
    LinearLayout mJoinLayout;
    @BindView(R.id.stu_head1)
    SimpleDraweeView mStuHead1;
    @BindView(R.id.stu_head2)
    SimpleDraweeView mStuHead2;
    @BindView(R.id.stu_head3)
    SimpleDraweeView mStuHead3;
    @BindView(R.id.headers)
    LinearLayout mHeaders;
    @BindView(R.id.tv_gpa)
    TextView mTvGpa;
    @BindView(R.id.tv_place)
    TextView mTvPlace;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.tv_academy)
    TextView mTvAcademy;
    @BindView(R.id.btn_sign)
    Button mBtnSign;
    @BindView(R.id.course_content)
    TextView mCourseContent;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.sign_layout)
    LinearLayout signLayout;

    private WifiManager mWifiManager;
    private int courseid = -1;
    private boolean isMine;
    private int flag = -1000;
    private int number = -1;
    private String ssid;
    private String bssid;
    private Action_PopUp action_popUp;
    private Comfirm_PopUp comfirm_popUp;
    private Map<String, Object> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.bind(this);
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


        AppManager.getAppManager().addActivity(this);
        initView();
    }


    private void initView() {
        courseid = Integer.parseInt(getIntent().getStringExtra("courseid"));
        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
        mProgressBar.setVisibility(View.VISIBLE);

        mSwipe.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                Toast.makeText(CourseDetailActivity.this, "正在加载中，请稍后...", Toast.LENGTH_SHORT).show();
                check(courseid);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipe.setRefreshing(true);
        check(courseid);
        Toast.makeText(this, "正在加载中，请稍后...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_action) {
            action_popUp = new Action_PopUp(isMine, CourseDetailActivity.this, new Pop_onclick());
            action_popUp.showAtLocation(findViewById(R.id.course_info), Gravity.BOTTOM, 0, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void check(int cid) {

        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.checkCourse(cid)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ObjEntity>() {
                    @Override
                    public void onCompleted() {
                        //TODO 显示签到button
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CourseDetailActivity.this,
                                "检查课程信息失败!", Toast.LENGTH_SHORT)
                                .show();
                        getCourseDetail(courseid);
                    }

                    @Override
                    public void onNext(ObjEntity objEntity) {
                        if (objEntity.isError()) {
                            Toast.makeText(CourseDetailActivity.this,
                                    "检查课程信息失败: " + objEntity.getMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            if (objEntity.getMsg().equals("yes")) {
                                isMine = true;
                            } else {
                                isMine = false;
                            }
                        }
                        getCourseDetail(courseid);
                    }
                });

    }


    private void getCourseDetail(final int courseid) {
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.getCourseInfo(courseid)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CourseinfoEntity>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        mSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CourseDetailActivity.this, "获取课程详情失败，请稍后重试1",
                                Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        mSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onNext(CourseinfoEntity courseinfoEntity) {
                        if (courseinfoEntity.isError()) {
                            Toast.makeText(CourseDetailActivity.this, "获取课程详情失败，请稍后重试" +
                                    courseinfoEntity.getMsg(), Toast.LENGTH_SHORT).show();

                        } else {
                            Message message = mHandler.obtainMessage();
                            message.what = REFRESHVIEW;
                            message.obj = courseinfoEntity;
                            message.sendToTarget();
                        }

                    }
                });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESHVIEW:
                    freshView((CourseinfoEntity) msg.obj);
                    break;
            }

        }
    };

    private void freshView(CourseinfoEntity courseinfoEntity) {
        bean = courseinfoEntity.getData();
        mTvWifi.setText(bean.getWifi());
        mTvGpa.setText(bean.getGpa());
        mTeacherName.setText(bean.getTeacher());
        mTvCoursename.setText(bean.getC_name());
        String teacherHead = bean.getHeader();
        if (teacherHead != null) {
            mTeacherHead.setImageURI(Uri.parse(teacherHead));

        }
        mTvCreate.setText(bean.getCreateTime().split(" ")[0]);
        mTvNum.setText(bean.getNumber());
        mCourseContent.setText(bean.getContent());
        mTvPlace.setText(bean.getPlace());
        mTvTime.setText(bean.getTime());
        mTvAcademy.setText(bean.getA_name().substring(0, bean.getA_name().indexOf("学院")));
        String stuheads = bean.getHeader_con();
        if (stuheads == null || stuheads.isEmpty()) {
            mHeaders.setVisibility(View.GONE);
        } else {
            String[] heads = stuheads.split(";");
            if (heads.length == 1) {
                mStuHead1.setImageURI(Uri.parse(heads[0]));
                mStuHead2.setVisibility(View.GONE);
                mStuHead3.setVisibility(View.GONE);
            } else if (heads.length == 2) {
                mStuHead1.setImageURI(Uri.parse(heads[0]));
                mStuHead2.setImageURI(Uri.parse(heads[1]));
                mStuHead3.setVisibility(View.GONE);
            } else {
                mStuHead1.setImageURI(Uri.parse(heads[0]));
                mStuHead2.setImageURI(Uri.parse(heads[1]));
                mStuHead3.setImageURI(Uri.parse(heads[2]));
            }
        }

        number = Integer.parseInt(bean.getNumber());
        int state = Integer.parseInt(bean.getState());
        String course_state = "";
        switch (state) {
            case 0:
                course_state = "未开始";
                break;
            case 1:
                course_state = "上课中";
                break;
            case 2:
                course_state = "已结束";
                break;
        }
        mTvState.setText(course_state);
        flag = Integer.parseInt(bean.getSignFlag());
    }


    @OnClick({R.id.join_layout, R.id.btn_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.join_layout:
                if (number > 0) {
                    Intent intent = new Intent(CourseDetailActivity.this,
                            SignHistoryActivity.class);
                    intent.putExtra("cid", courseid);
                    intent.putExtra("coursename", mTvCoursename.getText().toString());
                    Log.d(TAG, "onViewClicked: " + courseid);
                    startActivity(intent);
                    Toast.makeText(this, "签到记录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_sign:
                if (flag == 0) {
                    Toast.makeText(this, "上课时间还没到," +
                            "过会儿再来签到吧 :)", Toast.LENGTH_SHORT).show();
                } else if (flag == 2) {
                    Toast.makeText(this, "已经过了签到时间 :("
                            , Toast.LENGTH_SHORT).show();
                } else if (flag == 1) {
                    if (getWifi())
                        sign();
                }
                break;
        }
    }


    public class Pop_onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.join_btn:
                    if (bean != null) {
                        joinOrQuitCourse();
                    }
                    action_popUp.dismiss();
                    break;
                case R.id.quit_btn:
                    action_popUp.dismiss();
                    comfirm_popUp = new Comfirm_PopUp(CourseDetailActivity.this, new Pop_onclick());
                    comfirm_popUp.showAtLocation(findViewById(R.id.course_info), Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.comfirm_btn:
                    joinOrQuitCourse();
                    comfirm_popUp.dismiss();
                    break;
            }
        }

    }


    private void joinOrQuitCourse() {
        //TODO INT CID
        if (courseid != -1) {
            API api = HttpControl.getInstance().getRetrofit()
                    .create(API.class);
            Observable<ArrayEntity> observable;
            if (isMine) {
                observable =
                        api.quitCourse(courseid);
            } else {
                observable =
                        api.joinCourse(courseid);
            }


            observable.subscribeOn(io())
                    .unsubscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayEntity>() {
                        @Override
                        public void onCompleted() {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(CourseDetailActivity.this, "Error" + e.getMessage() +
                                    "请稍后再试 :(" +
                                    "", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onNext(ArrayEntity arrayEntity) {
                            if (arrayEntity.isError()) {
                                Toast.makeText(CourseDetailActivity.this, arrayEntity.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                if (arrayEntity.getMsg().equals("ok")) {
                                    if (isMine) {
                                        Toast.makeText(CourseDetailActivity.this, "退出课程成功",
                                                Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(CourseDetailActivity.this, "加入课程成功",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    mSwipe.setRefreshing(true);
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    check(courseid);
                                }
                            }

                        }
                    });
        }
    }


    private void sign() {
        String action;
        if (mBtnSign.getText().toString().equals("签到")) {
            action = "signin";
        } else {
            action = "signout";
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mBtnSign.setEnabled(false);
        Toast.makeText(this, "正在" + mBtnSign.getText().toString() + ",请稍后...", Toast.LENGTH_SHORT).show();


        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);

        api.studentSign(String.valueOf(courseid), bssid, action)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayEntity>() {
                    @Override
                    public void onCompleted() {
                        mBtnSign.setEnabled(true);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CourseDetailActivity.this, mBtnSign.getText().toString() +
                                "失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        mBtnSign.setEnabled(true);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ArrayEntity arrayEntity) {
                        if (arrayEntity.isError()) {
                            Toast.makeText(CourseDetailActivity.this, mBtnSign.getText().toString()
                                    + "失败" + arrayEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CourseDetailActivity.this, mBtnSign.getText().toString()
                                    + "成功", Toast.LENGTH_SHORT).show();
                            if (mBtnSign.getText().toString().equals("签到"))
                                mBtnSign.setText("签出");
                            else
                                mBtnSign.setText("签到");
                        }
                    }
                });
    }


    private boolean getWifi() {
        Toast.makeText(this, "正在搜寻wifi,请稍后", Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.VISIBLE);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = mWifiManager.getConnectionInfo();
        if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Toast.makeText(this, "请打开wifi并连接后再点击", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (info.getNetworkId() == -1) {
                Toast.makeText(this, "请连接wifi后再点击", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                ssid = info.getSSID();
                bssid = info.getBSSID();
            }
        }
        mProgressBar.setVisibility(View.GONE);
        return true;
    }
}
