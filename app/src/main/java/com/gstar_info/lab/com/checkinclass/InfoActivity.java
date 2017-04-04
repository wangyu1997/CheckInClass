package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.gstar_info.lab.com.checkinclass.utils.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_first)
    LinearLayout layoutFirst;
    @BindView(R.id.layout_second)
    LinearLayout layoutSecond;
    @BindView(R.id.layout_third)
    LinearLayout layoutThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
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

    public void init() {
        layoutSecond.setBackground(getResources().getDrawable(R.color.amber_50));
    }


    @OnClick({R.id.layout_first, R.id.layout_third})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_first:
                startActivity(new Intent(this, HomePageActivity.class));
                break;
            case R.id.layout_third:
                startActivity(new Intent(this, PersonActivity.class));
                break;
        }
    }
}
