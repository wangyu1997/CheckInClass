package com.gstar_info.lab.com.checkinclass.Api;

import com.gstar_info.lab.com.checkinclass.model.AcademysEntity;
import com.gstar_info.lab.com.checkinclass.model.LoginEntity;
import com.gstar_info.lab.com.checkinclass.model.MajorEntity;
import com.gstar_info.lab.com.checkinclass.model.RegisterEntity;
import com.gstar_info.lab.com.checkinclass.model.StringEntity;
import com.gstar_info.lab.com.checkinclass.model.courseShowEntity;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ritchiehuang on 4/1/17.
 */

public interface API {
    String BASE_URL = "http://119.29.97.151/BigClass/";


    //注册
    @FormUrlEncoded
    @POST("token/getToken.php")
    Call<String> getToken(@Field("token") String token);


    //学生注册接口

    @FormUrlEncoded
    @POST("student/register.php")
    Observable<RegisterEntity> studentRegister(@Field("username")
                                                       String username,
                                               @Field("password")
                                                       String password,
                                               @Field("mid")
                                                       int mid,
                                               @Field("name")
                                                       String name,
                                               @Field("sex")
                                                       int sex,
                                               @Field("device")
                                                       String device,
                                               @Field("class")
                                                       String classX
    );

    //学生登录接口
    @FormUrlEncoded
    @POST("student/login.php")
    Observable<LoginEntity> studentLogin(@Field("username")
                                                 String username,
                                         @Field("password")
                                                 String password,
                                         @Field("device")
                                                 String device);


    //学院接口
    @POST("public/academys.php")
    Observable<AcademysEntity> getAcademys();


    //专业接口
    @FormUrlEncoded
    @POST("public/majors.php")
    Observable<MajorEntity> getMajor(@Field("id") int academyid);

    //注册 请求验证码接口
    @FormUrlEncoded
    @POST("public/sendMail.php")
    Observable<StringEntity> getcode(@Field("action") String acton, @Field("email") String email);

    //注册 请求验证码接口
    @FormUrlEncoded
    @POST("public/sendMail.php")
    Observable<StringEntity> resetPass(@Field("action") String acton, @Field("email") String email, @Field("flag") String flag);


    //首页课程
    @FormUrlEncoded
    @POST("public/allcourses.php")
    Observable<courseShowEntity> getCourses(@Field("aid") int aid);


}