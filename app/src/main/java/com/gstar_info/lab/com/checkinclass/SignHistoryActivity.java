package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.adapter.ListSignHistoryItemAdapter;
import com.gstar_info.lab.com.checkinclass.model.SignHistoryEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.DiveiderItemDecoration;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;
import com.gstar_info.lab.com.checkinclass.utils.ScrollUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

public class SignHistoryActivity extends AppCompatActivity {


    private static final String TAG = "SignHistoryActivity";


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_coursename)
    TextView mTvCoursename;
    @BindView(R.id.head_layout)
    LinearLayout mHeadLayout;
    @BindView(R.id.recycler_signlist)
    RecyclerView mRecyclerSignlist;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;


    private List<SignHistoryEntity.DataBean> recycler_signlist;
    private ListSignHistoryItemAdapter mSignHistoryItemAdapter;
    private int cid = -1;
    private FootTextInterFace interFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_history);
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


        init();

    }

    private void init() {
        cid = getIntent().getIntExtra("cid", -1);
        mTvCoursename.setText(getIntent().getStringExtra("coursename"));
        Log.d(TAG, "getSignHistoryList: " + cid);

        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
        mProgressBar.setVisibility(View.VISIBLE);

        recycler_signlist = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mSignHistoryItemAdapter = new ListSignHistoryItemAdapter(this, recycler_signlist);
        mRecyclerSignlist.setAdapter(mSignHistoryItemAdapter);
        mRecyclerSignlist.setLayoutManager(linearLayoutManager);
        mRecyclerSignlist.addItemDecoration(new DiveiderItemDecoration(this,
                DiveiderItemDecoration.VERTICAL_LIST));
        mRecyclerSignlist.setHasFixedSize(true);
        mRecyclerSignlist.setItemAnimator(new DefaultItemAnimator());


        mSwipe.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                Toast.makeText(SignHistoryActivity.this, "正在加载中，请稍后...", Toast.LENGTH_SHORT).show();
                getSignHistoryList(cid);
            }


        });

        interFace = new FootTextInterFace() {
            @Override
            public void setText(TextView tv) {
                if (recycler_signlist.size() == 0) {
                    tv.setText("暂无数据");
                } else {
                    tv.setText("加载完了");
                }
            }
        };

        mSignHistoryItemAdapter.setInterFace(interFace);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipe.setRefreshing(true);
        getSignHistoryList(cid);
    }


    private void getSignHistoryList(int cid) {
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Log.d(TAG, "getSignHistoryList: " + cid);
        api.getSignHistory(cid)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignHistoryEntity>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(SignHistoryActivity.this,
                                "加载签到记录完成!", Toast.LENGTH_SHORT)
                                .show();
                        mProgressBar.setVisibility(View.GONE);
                        mSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SignHistoryActivity.this,
                                "加载签到记录错误!", Toast.LENGTH_SHORT)
                                .show();
                        mProgressBar.setVisibility(View.GONE);
                        mSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onNext(SignHistoryEntity signHistoryEntity) {
                        if (signHistoryEntity.isError()) {
                            Toast.makeText(SignHistoryActivity.this,
                                    "Error:" + signHistoryEntity.getMsg(), Toast.LENGTH_SHORT)
                                    .show();

                        } else {
                            recycler_signlist = signHistoryEntity.getData();
                            if (recycler_signlist == null || recycler_signlist.size() == 0)
                                recycler_signlist = new ArrayList<>();
                            mSignHistoryItemAdapter.setData(recycler_signlist);
                        }

                    }
                });

    }


}
