package com.gstar_info.lab.com.checkinclass.model;

import android.widget.Toast;


import com.gstar_info.lab.com.checkinclass.utils.MyApplication;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangyu on 03/04/2017.
 */

public class RegistVerEntity implements Serializable {
    private String username = "";
    private String email = "";
    private String password = "";
    private String passwordCom = "";
    private String name = "";
    private int sex = -1;
    private String header = "";
    private int aid = -1;
    private int mid = -1;
    private String classinfo = "";


    public RegistVerEntity(String username, String email, String password, String passwordCom, String name, int sex, String header) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.passwordCom = passwordCom;
        this.name = name;
        this.sex = sex;
        this.header = header;
    }

    public RegistVerEntity() {
    }


    public int getMid() {
        return mid;
    }

    public String getClassinfo() {
        return classinfo;
    }

    public void setClassinfo(String classinfo) {
        this.classinfo = classinfo;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordCom() {
        return passwordCom;
    }

    public void setPasswordCom(String passwordCom) {
        this.passwordCom = passwordCom;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isLeagel1() {
        if (username.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sex == -1) {
            Toast.makeText(MyApplication.getGlobalContext(), "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (aid == -1) {
            Toast.makeText(MyApplication.getGlobalContext(), "请选择学院", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isLeagel12() {
        if (username.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sex == -1) {
            Toast.makeText(MyApplication.getGlobalContext(), "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (aid == -1) {
            Toast.makeText(MyApplication.getGlobalContext(), "请选择学院", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mid == -1) {
            Toast.makeText(MyApplication.getGlobalContext(), "请选择专业", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (classinfo.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请选择班级", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isLeagel2() {
        if (email.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入邮箱", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkEmaile(email)) {
            Toast.makeText(MyApplication.getGlobalContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordCom.isEmpty()) {
            Toast.makeText(MyApplication.getGlobalContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordCom.equals(password)) {
            Toast.makeText(MyApplication.getGlobalContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(MyApplication.getGlobalContext(), "密码必须超过8位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 正则表达式校验邮箱
     *
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    private static boolean checkEmaile(String emaile) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }
}


