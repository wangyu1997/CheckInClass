package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.adapter.ListAcademyGroupItemAdapter;
import com.gstar_info.lab.com.checkinclass.adapter.ListMajorGroupItemAdapter;
import com.gstar_info.lab.com.checkinclass.model.MajorEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;

/**
 * Created by ritchiehuang on 9/2/16.
 */

public class MajorSelectActivity extends AppCompatActivity {

    private static final String TAG = "MajorSelectActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.major_list)
    ListView mMajorList;
    private AppCompatActivity context;
    private List<MajorEntity.DataBean> dataList;
    private ListMajorGroupItemAdapter adapter;
    private boolean flag;

    private int academy_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        setContentView(R.layout.activity_major_select);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);

        initData();
        dataList = new ArrayList<>();
        getSchoolList();
        if (getIntent().getIntExtra("flag", -1) != -1)
            flag = true;
    }

    private void initData() {
        academy_id = getIntent().getIntExtra("academy_id", -1);
    }


    public void getSchoolList() {
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        if (academy_id != -1) {
            api.getMajor(academy_id)
                    .subscribeOn(io())
                    .unsubscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<MajorEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MajorSelectActivity.this, "Error", Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onNext(MajorEntity majorEntity) {

                            if (!majorEntity.isError()) {

                                initSchoolList(majorEntity.getData());
                            }else{
                                Toast.makeText(MajorSelectActivity.this,"Error"+majorEntity.getMsg(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }

    }

    public void initSchoolList(List<MajorEntity.DataBean> data) {
        Log.d(TAG, "initSchoolList: " + data.size());
        adapter = new ListMajorGroupItemAdapter(context, data);
        if (flag) {
            adapter.setFlag(flag);
        }
        mMajorList.setAdapter(adapter);

    }


    @OnClick(R.id.toolbar)
    public void onViewClicked() {
        this.finish();
    }
}
