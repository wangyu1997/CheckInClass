package com.gstar_info.lab.com.checkinclass.Dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gstar_info.lab.com.checkinclass.Api.API;
import com.gstar_info.lab.com.checkinclass.R;
import com.gstar_info.lab.com.checkinclass.model.ArrayEntity;
import com.gstar_info.lab.com.checkinclass.utils.HttpControl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static rx.schedulers.Schedulers.io;


public class JoinCourseDialog extends DialogFragment {

    public static final String TAG = JoinCourseDialog.class.getSimpleName();
    public static final String ARG_ICON = "icon";
    public static final String ARG_TITLE = "title";
    public static final String CID = "cid";
    public static final String COURSE_NAME = "coursename";
    public static final String TEACHER_HEAD = "teacherHead";
    @BindView(R.id.tv_coursename)
    TextView mTvCoursename;
    @BindView(R.id.img_teacherhead)
    SimpleDraweeView mImgTeacherhead;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    Unbinder unbinder1;

    private boolean flag = false;



    public JoinCourseDialog() {
    }

    public static JoinCourseDialog newInstance(int icon, String title, String cid, String
            courseName, String teacherHead) {
        return newInstance(icon, title, cid, courseName, teacherHead, null);
    }

    public static JoinCourseDialog newInstance(int icon, String title, String cid,
                                               String courseName, String teacherHead,
                                               Fragment fragment) {
        JoinCourseDialog confirmDialog = new JoinCourseDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_ICON, icon);
        args.putString(ARG_TITLE, title);
        args.putString(CID, cid);
        args.putString(COURSE_NAME, courseName);
        args.putString(TEACHER_HEAD, teacherHead);
        confirmDialog.setArguments(args);
        if (fragment != null) confirmDialog.setTargetFragment(fragment, 0);
        return confirmDialog;
    }

    @SuppressLint("InflateParams")
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_joincourse, null);


        unbinder1 = ButterKnife.bind(this, dialogView);

        mImgTeacherhead.setImageURI(Uri.parse(getArguments().getString(TEACHER_HEAD)));
        mTvCoursename.setText(getArguments().getString(COURSE_NAME));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString(ARG_TITLE))
                .setIcon(getArguments().getInt(ARG_ICON))
                .setView(dialogView)
                .setPositiveButton("确认", new OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        joinCourse();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void joinCourse() {
        String cid = getArguments().getString(CID);
        Toast.makeText(getActivity(), "" + cid, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "joinCourse: " + cid);
        if (cid != null) {
            API api = HttpControl.getInstance().getRetrofit()
                    .create(API.class);
            int id = Integer.parseInt(cid);
            api.joinCourse(id)
                    .subscribeOn(io())
                    .unsubscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayEntity>() {
                        @Override
                        public void onCompleted() {

                            mProgressBar.setVisibility(View.GONE);


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), "Error 加入课程失败，" + e.getMessage() +
                                    "请稍后再试 :(" +
                                    "", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onNext(ArrayEntity arrayEntity) {
                            if (arrayEntity.isError()) {
                                Toast.makeText(getActivity(), "加入课程失败，" +
                                        "请稍后再试 :(" +
                                        arrayEntity.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                if (arrayEntity.getMsg().equals("ok")) {

                                    flag = true;

                                }
                            }

                        }
                    });
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

}