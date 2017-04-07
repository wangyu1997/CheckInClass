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
import com.gstar_info.lab.com.checkinclass.Dialog.JoinCourseDialog;
import com.gstar_info.lab.com.checkinclass.adapter.ListCourseItemAdapter;
import com.gstar_info.lab.com.checkinclass.model.courseShowEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;
import com.gstar_info.lab.com.checkinclass.utils.ScrollUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.layout_first)
    LinearLayout layoutFirst;
    @BindView(R.id.layout_second)
    LinearLayout layoutSecond;
    @BindView(R.id.layout_third)
    LinearLayout layoutThird;
    @BindView(R.id.course_list)
    RecyclerView courseList;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout idSwipeLy;
    @BindView(R.id.tabview)
    RelativeLayout tabview;
    @BindView(R.id.progressBar4)
    ProgressBar progressBar;

    private ListCourseItemAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private int aid;
    private String aname;
    private Intent data;
    public static final int Academy_res = 600;
    public final int Academy_req = 613;
    public static final int Major_res = 0x005;
    public final int Major_req = 0x006;
    private FootTextInterFace interFace;
    private List<courseShowEntity.DataBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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
        layoutFirst.setBackground(getResources().getDrawable(R.color.amber_50));
        manager = new LinearLayoutManager(this);
        courseList.setLayoutManager(manager);
        data = getIntent();
        datas = new ArrayList<>();
        aid = Integer.parseInt(data.getStringExtra("aid"));
        aname = data.getStringExtra("aname");
        adapter = new ListCourseItemAdapter(HomePageActivity.this, datas, aid, aname);
        adapter.setRecyclerOnItemClickListener(new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String courseid = datas.get(position - 1).getId();
                Intent intent = new Intent(HomePageActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseid", courseid);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

                //TODO  加入课程
                String courseid = datas.get(position - 1).getId();
                String courseName = datas.get(position-1).getC_name();
                String teacherHead = datas.get(position-1).getHeader();
                JoinCourseDialog.newInstance(R.mipmap.zuoye,
                        "添加课程", courseid,courseName,teacherHead)
                        .show(getFragmentManager(), JoinCourseDialog.TAG);
            }
        });
        courseList.setAdapter(adapter);
        idSwipeLy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        idSwipeLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                getCourse(aid);
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
        progressBar.setVisibility(View.VISIBLE);
        adapter.setInterFace(interFace);
        courseList.setOnScrollListener(new ScrollUtil.inVisibleScorllListener() {
            @Override
            public void onHide() {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tabview.getLayoutParams();
                int fabBottomMargin = lp.bottomMargin;
                tabview.animate().translationY(tabview.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }

            @Override
            public void onShow() {
                tabview.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        idSwipeLy.setRefreshing(true);
        getCourse(aid);
    }

    @OnClick({R.id.layout_second, R.id.layout_third})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_second:
                startActivity(new Intent(this, InfoActivity.class));
                break;
            case R.id.layout_third:
                startActivity(new Intent(this, PersonActivity.class));
                break;
        }
    }

    public void getCourse(int aid) {

        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.getCourses(aid)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<courseShowEntity>() {
                    @Override
                    public void onCompleted() {
                        idSwipeLy.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(HomePageActivity.this, "获取课程信息失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        idSwipeLy.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(courseShowEntity courseShowEntity) {
                        if (courseShowEntity.isError()) {
                            Toast.makeText(HomePageActivity.this, "获取课程信息失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        } else {
                            datas = courseShowEntity.getData();
                            if (datas == null || datas.size() == 0)
                                datas = new ArrayList<>();
                            adapter.setData(datas);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Academy_req && resultCode == Academy_res) {
            this.aid = data.getIntExtra("academy_id", -1);
            this.aname = data.getStringExtra("academy_name");
            if (aid != -1 && !aname.isEmpty() && adapter != null) {
                adapter.setAacademy(aid, aname);
            }
        }
    }

}
