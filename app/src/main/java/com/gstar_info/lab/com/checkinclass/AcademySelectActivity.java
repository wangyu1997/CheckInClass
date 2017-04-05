package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.adapter.ListAcademyGroupItemAdapter;
import com.gstar_info.lab.com.checkinclass.model.AcademysEntity;
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

/**
 * Created by wangyu on 9/2/16.
 */

public class AcademySelectActivity extends AppCompatActivity {


    @BindView(R.id.academy_group_list)
    ListView academyGroupList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AppCompatActivity context;
    private List<AcademysEntity.DataBean> dataList;
    private ListAcademyGroupItemAdapter adapter;

    private boolean flag;

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
        setContentView(R.layout.activity_academy_select);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        dataList = new ArrayList<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSchoolList();
        if (getIntent().getIntExtra("flag", -1) != -1) {
            flag = true;
        }
    }


    public void getSchoolList() {
        Retrofit retrofit = HttpControl.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        api.getAcademys()
                .subscribeOn(io())
                .unsubscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AcademysEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AcademySelectActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AcademysEntity academyListModel) {
                        if (!academyListModel.isError()) {
                            initSchoolList(academyListModel.getData());
                        } else {
                            Toast.makeText(AcademySelectActivity.this, "Error"
                                    + academyListModel.getMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void initSchoolList(List<AcademysEntity.DataBean> data) {
        adapter = new ListAcademyGroupItemAdapter(this, data);
        if (flag) {
            adapter.setFlag(flag);
        }
        academyGroupList.setAdapter(adapter);

    }


}
