package com.gstar_info.lab.com.checkinclass.Api;

import com.gstar_info.lab.com.checkinclass.model.AcademysEntity;
import com.gstar_info.lab.com.checkinclass.model.ArrayEntity;
import com.gstar_info.lab.com.checkinclass.model.CourseinfoEntity;
import com.gstar_info.lab.com.checkinclass.model.LoginEntity;
import com.gstar_info.lab.com.checkinclass.model.MajorEntity;
import com.gstar_info.lab.com.checkinclass.model.ObjEntity;
import com.gstar_info.lab.com.checkinclass.model.SignHistoryEntity;
import com.gstar_info.lab.com.checkinclass.model.StringEntity;
import com.gstar_info.lab.com.checkinclass.model.courseShowEntity;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ritchiehuang on 4/1/17.
 */

public interface API {
    String BASE_URL = "http://119.29.97.151/BigClass1/";


    //注册
    @FormUrlEncoded
    @POST("token/getToken.php")
    Call<String> getToken(@Field("token") String token);


    //学生注册接口

    @FormUrlEncoded
    @POST("student/regist.php")
    Observable<ObjEntity> studentRegister(@Field("username")
                                                  String username,
                                          @Field("password")
                                                  String password,
                                          @Field("mid")
                                                  int mid,
                                          @Field("email")
                                                  String email,
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

    //重置密码 请求验证码接口
    @FormUrlEncoded
    @POST("public/sendMail.php")
    Observable<StringEntity> resetPass(@Field("action") String acton, @Field("email") String email, @Field("flag") String flag);

    //上传头像
    @FormUrlEncoded
    @POST("student/upHeader.php")
    Observable<ArrayEntity> upHeader(@Field("head") String headUrl);

    //忘记密码
    @FormUrlEncoded
    @POST("teacher/forgetPass.php")
    Observable<ArrayEntity> forgetPass(@Field("email") String email, @Field("password") String password);


    //首页课程
    @FormUrlEncoded
    @POST("public/allcourses.php")
    Observable<courseShowEntity> getCourses(@Field("aid") int aid);


    //检查课程参加是否已参加
    @FormUrlEncoded
    @POST("student/check.php")
    Observable<ObjEntity> checkCourse(@Field("cid") int cid);


    //课程详情
    @FormUrlEncoded
    @POST("public/Info.php")
    Observable<CourseinfoEntity> getCourseInfo(@Field("id") int id);

    @FormUrlEncoded
    @POST("public/detail.php")
    Observable<SignHistoryEntity> getSignHistory(@Field("cid") int cid);

    //学生签到
    @FormUrlEncoded
    @POST("student/sign.php")
    Observable<ArrayEntity> studentSign(@Field("cid") String cid,
                                        @Field("bssid") String bssid,
                                        @Field("action") String action);

    //学生课程历史
    @POST("student/my.php")
    Observable<courseShowEntity> getCourseHistory();

    //加入课程
    @FormUrlEncoded
    @POST("student/join.php")
    Observable<ArrayEntity> joinCourse(@Field("cid") int cid);

    //退出课程
    @FormUrlEncoded
    @POST("student/out.php")
    Observable<ArrayEntity> quitCourse(@Field("cid") int cid);

}