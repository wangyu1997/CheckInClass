package com.gstar_info.lab.com.checkinclass.model;

/**
 * Created by ritchiehuang on 4/1/17.
 */

public class StudentInfoBean {
    private String uid;
    private String username;
    private String aid;
    private String a_name;
    private String mid;
    private String m_name;
    private String name;
    private String sex;
    private String classX;
    private String createTime;


    public StudentInfoBean() {
    }

    public StudentInfoBean(LoginEntity.DataBean dataBean) {
        uid = dataBean.getUid();
        username = dataBean.getUsername();
        aid = dataBean.getAid();
        a_name = dataBean.getA_name();
        mid = dataBean.getMid();
        m_name = dataBean.getM_name();
        name = dataBean.getName();
        sex = dataBean.getSex();
        classX = dataBean.getClassX();
        createTime = dataBean.getCreateTime();

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
