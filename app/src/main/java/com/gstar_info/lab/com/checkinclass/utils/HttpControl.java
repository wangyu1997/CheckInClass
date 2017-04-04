package com.gstar_info.lab.com.checkinclass.utils;


import com.gstar_info.lab.com.checkinclass.Api.API;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class HttpControl {

    private volatile static HttpControl mInstance;
    private static Retrofit retrofit;
    private static OkHttpClient client;

    private HttpControl() {

        client = getUnsafeOkHttpClient();

//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl((API.BASE_URL))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static HttpControl getInstance() {
        if (mInstance == null) {
            synchronized (HttpControl.class) {
                if (mInstance == null)
                    mInstance = new HttpControl();
            }
        }
        return mInstance;
    }

    public OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("token", MyApplication.getToken())
                            .addHeader("Cache-Control", "public, max-age=900")
                            .build();
                    return chain.proceed(request);
                }
            };

            File cacheDirectory = new File(MyApplication.getGlobalContext()
                    .getCacheDir().getAbsolutePath(), "HttpCache");
            Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .cookieJar(MyApplication.getCookieJar())
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)
                    .cache(cache)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public OkHttpClient getClient() {
        if (client == null) {
            client = HttpControl.getInstance().initClient();
        }
        return client;

    }

    private OkHttpClient initClient() {
        return client;
    }

    public Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = HttpControl.getInstance().initRetrofit();
        }
        return retrofit;
    }

    private Retrofit initRetrofit() {
        return retrofit;
    }
}
