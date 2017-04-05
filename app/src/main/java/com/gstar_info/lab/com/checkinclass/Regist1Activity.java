package com.gstar_info.lab.com.checkinclass;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.gstar_info.lab.com.checkinclass.PopUpWindow.Class_PopUp;
import com.gstar_info.lab.com.checkinclass.model.RegistVerEntity;
import com.gstar_info.lab.com.checkinclass.utils.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.logger.LogTool;


/**
 * Created by wangyu on 02/04/2017.
 */

public class Regist1Activity extends AppCompatActivity {

    private static final String TAG = "Regist1Activity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_user)
    EditText mEditUser;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.im_male)
    ImageView mImMale;
    @BindView(R.id.im_female)
    ImageView mImFemale;
    @BindView(R.id.edit_academy)
    EditText mEditAcademy;
    @BindView(R.id.edit_major)
    EditText mEditMajor;
    @BindView(R.id.edit_class)
    EditText mEditClass;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    private RegistVerEntity mRegistVerEntity;
    private Class_PopUp class_popUp;
    private static final int academy_req = 581;
    public static final int academy_res = 100;
    private static final int major_req = 0x0001;
    public static final int major_res = 0x0002;
    public String academy_name;
    public String major_name;
    public String classinfo;
    public int academy_id;
    public int major_id;
    public String sexStr;
    public int sex;//男1 女2
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;


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
        setContentView(R.layout.activity_register1);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        mSharedPreferences = this.getSharedPreferences("aid_sp", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
        mRegistVerEntity = new RegistVerEntity();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectSex(1);
        mEditClass.setKeyListener(null);
        mEditAcademy.setKeyListener(null);
        mEditMajor.setKeyListener(null);
        mEditAcademy.setText("");
        mEditAcademy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditMajor.getText() != null) {
                    mEditMajor.setText("");
                }
            }
        });
    }


    public void selectSex(int flag) {
        mImFemale.setImageDrawable(getResources().getDrawable(R.mipmap.femalehide));
        mImMale.setImageDrawable(getResources().getDrawable(R.mipmap.malehide));
        if (flag == 1) {
            mRegistVerEntity.setSex(1);
            mImMale.setImageDrawable(getResources().getDrawable(R.mipmap.maleshow));
        }
        if (flag == 2) {
            mRegistVerEntity.setSex(2);
            mImFemale.setImageDrawable(getResources().getDrawable(R.mipmap.femaleshow));
        }
    }


    @OnClick({R.id.edit_academy, R.id.edit_major, R.id.edit_class, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_academy:
                startActivityForResult(new Intent(this, AcademySelectActivity.class),
                        academy_req);
                break;
            case R.id.edit_major:
                int academyid = mSharedPreferences.getInt("academy_id", -1);
                if (academyid == -1 || mEditAcademy.getText().equals("")) {
                    Toast.makeText(Regist1Activity.this, "请先选择专业!", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    Intent intent = new Intent(this, MajorSelectActivity.class);
                    intent.putExtra("academy_id", academyid);
                    Log.d(TAG, "onViewClicked: " + academyid);
                    startActivityForResult(intent, major_req);

                }
                break;
            case R.id.edit_class:
                class_popUp = new Class_PopUp(this, new PopClick(), new valueChangeListener());
                classinfo = Class_PopUp.data[Class_PopUp.DEFAULT_VALUE];
                class_popUp.showAtLocation(this.findViewById(R.id.regist1), Gravity.BOTTOM, 0, 0);
                break;

            case R.id.btn_next:
                mRegistVerEntity.setUsername(mEditUser.getText().toString());
                mRegistVerEntity.setName(mEditName.getText().toString());

                if (mRegistVerEntity.isLeagel12()) {
                    Intent intent = new Intent(Regist1Activity.this, Regist2Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("regist", mRegistVerEntity);
                    intent.putExtra("bundle", bundle);
                    intent.putExtra("classinfo", classinfo);
                    intent.putExtra("major_name", major_name);
                    intent.putExtra("mid", major_id);
                    startActivity(intent);
                }
                break;
        }
    }


    public class valueChangeListener implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            classinfo = Class_PopUp.data[picker.getValue()];
        }
    }

    public class PopClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_cancel:
                    class_popUp.dismiss();
                    break;
                case R.id.pop_confirm:
                    if (classinfo != null) {
                        class_popUp.dismiss();
                        mEditClass.setText(classinfo);
                        mRegistVerEntity.setClassinfo(classinfo);
                    }
                    break;

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == academy_req) && (resultCode == academy_res)) {
            academy_name = data.getStringExtra("academy_name");
            academy_id = data.getIntExtra("academy_id", -1);
            mEditor.putInt("academy_id", academy_id);
            mEditor.commit();
            mEditAcademy.setText(academy_name);
            mRegistVerEntity.setAid(academy_id);
        }

        if ((requestCode == major_req) && (resultCode == major_res)) {
            major_name = data.getStringExtra("major_name");
            major_id = data.getIntExtra("major_id", -1);
            mEditMajor.setText(major_name);
            mRegistVerEntity.setMid(major_id);

        }
    }

    @OnClick({R.id.im_male, R.id.im_female, R.id.edit_academy})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.im_male:
                selectSex(1);
                break;
            case R.id.im_female:
                selectSex(2);
                break;
        }
    }
}
