package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.adapter.ListCourseItemAdapter;
import com.gstar_info.lab.com.checkinclass.model.CourseHistoryEntity;
import com.gstar_info.lab.com.checkinclass.model.courseShowEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

public class CourseHistoryActivity extends AppCompatActivity {

    private static final String TAG = "CourseHistoryActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_coursehistroylist)
    RecyclerView mRecyclerCoursehistroylist;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;


    private ListCourseItemAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private FootTextInterFace interFace;
    private List<CourseHistoryEntity.DataBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_history);
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


        init();
    }

    private void init() {
        //TODO SET the first activity selected
        manager = new LinearLayoutManager(this);
        mRecyclerCoursehistroylist.setLayoutManager(manager);
        datas = new ArrayList<>();
        adapter = new ListCourseItemAdapter(CourseHistoryActivity.this, datas, aid, aname);
        adapter.setRecyclerOnItemClickListener(new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String courseid = datas.get(position - 1).getId();
                Intent intent = new Intent(CourseHistoryActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseid", courseid);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                String courseid = datas.get(position - 1).getId();
                Intent intent = new Intent(CourseHistoryActivity.this, CourseDetailActivity.class);
                Toast.makeText(CourseHistoryActivity.this, " " + courseid, Toast.LENGTH_SHORT)
                        .show();
                intent.putExtra("courseid", courseid);
                startActivity(intent);
            }
        });
        mRecyclerCoursehistroylist.setAdapter(adapter);
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                getCourseHistory();
            }
        });
        interFace = new FootTextInterFace() {
            @Override
            public void setText(TextView tv) {
                if (datas.size() == 0) {
                    tv.setText("暂无数据");
                } else {
                    tv.setText("加载完了");
                }
            }
        };
        mProgressBar.setVisibility(View.VISIBLE);
        adapter.setInterFace(interFace);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipe.setRefreshing(true);
        getCourseHistory();
    }


    private void getCourseHistory() {

        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.getCourseHistory()
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CourseHistoryEntity>() {
                    @Override
                    public void onCompleted() {
                        mSwipe.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CourseHistoryActivity.this,"获取课程历史失败,请稍后再试!",
                                Toast.LENGTH_SHORT).show();
                        mSwipe.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(CourseHistoryEntity courseHistoryEntity) {
                        if (courseHistoryEntity.isError()) {
                            Toast.makeText(CourseHistoryActivity.this, "Error: " +
                                            courseHistoryEntity.getMsg(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            if (courseHistoryEntity.getData())
                        }
                    }
                });

    }


}
