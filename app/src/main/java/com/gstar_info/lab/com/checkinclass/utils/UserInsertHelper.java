package com.gstar_info.lab.com.checkinclass.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gstar_info.lab.com.checkinclass.model.LoginEntity;
import com.gstar_info.lab.com.checkinclass.model.StudentInfoBean;

import static android.os.Build.ID;


public class UserInsertHelper {

    public static final String UID = "uid";
    public static final String USERNAME = "username";
    public static final String AID = "aid";
    public static final String A_NAME = "a_name";
    public static final String MID = "mid";
    public static final String M_NAME = "m_name";
    public static final String NAME = "name";
    public static final String SEX = "sex";
    public static final String CLASS = "class";
    public static final String CREATETIME = "createTime";
    public static final String PASSWORD = "password";


    public static void insertUser(Context context, LoginEntity.DataBean dataBean, String password) {
        StudentInfoBean userInfoBean = new StudentInfoBean(dataBean);
        SharedPreferences.Editor editor = MyApplication.getEditor(context);
        editor.putString(UID, userInfoBean.getUid());
        editor.putString(NAME, userInfoBean.getName());
        editor.putString(SEX, userInfoBean.getSex());
        editor.putString(AID, userInfoBean.getAid());
        editor.putString(USERNAME, userInfoBean.getUsername());
        editor.putString(A_NAME, userInfoBean.getA_name());
        editor.putString(MID, userInfoBean.getMid());
        editor.putString(M_NAME, userInfoBean.getM_name());
        editor.putString(CLASS, userInfoBean.getClassX());
        editor.putString(CREATETIME, userInfoBean.getCreateTime());
        editor.putString(PASSWORD, password);
        editor.commit();
    }


    public static void updateUser(Context context, StudentInfoBean userInfoBean) {
        SharedPreferences.Editor editor = MyApplication.getEditor(context);
        editor.putString(UID, userInfoBean.getUid());
        editor.putString(NAME, userInfoBean.getName());
        editor.putString(SEX, userInfoBean.getSex());
        editor.putString(AID, userInfoBean.getAid());
        editor.putString(USERNAME, userInfoBean.getUsername());
        editor.putString(A_NAME, userInfoBean.getA_name());
        editor.putString(MID, userInfoBean.getMid());
        editor.putString(M_NAME, userInfoBean.getM_name());
        editor.putString(CLASS, userInfoBean.getClassX());
        editor.putString(CREATETIME, userInfoBean.getCreateTime());
        editor.commit();
    }

    public static StudentInfoBean getUserInfo(Context context) {
        SharedPreferences sharedPreferences = MyApplication.getSharedPreferences(context);
        if (sharedPreferences.getString(UID, null) != null) {
            StudentInfoBean userInfoBean = new StudentInfoBean();
            userInfoBean.setUid(sharedPreferences.getString(UID, null));
            userInfoBean.setName(sharedPreferences.getString(NAME, null));
            userInfoBean.setSex(sharedPreferences.getString(SEX, null));
            userInfoBean.setAid(sharedPreferences.getString(AID, null));
            userInfoBean.setMid(sharedPreferences.getString(USERNAME, null));
            userInfoBean.setA_name(sharedPreferences.getString(A_NAME, null));
            userInfoBean.setUsername(sharedPreferences.getString(USERNAME, null));
            userInfoBean.setM_name(sharedPreferences.getString(M_NAME, null));
            userInfoBean.setClassX(sharedPreferences.getString(CLASS, null));
            userInfoBean.setCreateTime(sharedPreferences.getString(CREATETIME, null));
            userInfoBean.setPassword(sharedPreferences.getString(PASSWORD, ""));
            return userInfoBean;
        }

        return null;
    }

    public static boolean isUserId(Context context, String id) {
        SharedPreferences sharedPreferences = MyApplication.getSharedPreferences(context);
        String userId = sharedPreferences.getString(UID, null);
        return id.equals(userId);
    }

    public static void removeUser(Context context) {
        SharedPreferences.Editor editor = MyApplication.getEditor(context);
        editor.remove(ID);
        editor.remove(AID);
        editor.remove(NAME);
        editor.remove(SEX);
        editor.remove(USERNAME);
        editor.remove(A_NAME);
        editor.remove(MID);
        editor.remove(M_NAME);
        editor.remove(CLASS);
        editor.remove(CREATETIME);
        editor.remove(PASSWORD);
        editor.commit();
    }


}
