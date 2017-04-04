package com.gstar_info.lab.com.checkinclass.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by FJS0420 on 2016/8/13.
 */

public class HttpUtil {

    private static OkHttpClient client = HttpControl.getInstance().getClient();


    //获取七牛上传凭证
    public static String getToken(String url) throws IOException {
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        String postBody = "Hello BigClass";
        String token = "";

        RequestBody body = new FormBody.Builder()
                .add("token",postBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }
        String jsonstr = response.body().string();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonstr);
            boolean error = (boolean) jsonObject.get("error");
            if (!error){
                jsonObject = jsonObject.getJSONObject("data");
                token = (String) jsonObject.get("token");
            }else {
                throw new JSONException("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return token;
    }




}
