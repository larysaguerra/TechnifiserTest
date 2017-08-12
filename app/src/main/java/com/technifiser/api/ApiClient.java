package com.technifiser.api;

import com.technifiser.TecnifiserApplication;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by guerra on 11/08/17.
 * Util configure client and adapter retrofit.
 */

public class ApiClient {

    private static String ACCEPT_LANGUAGE = "Accept-Language";
    private static String URL_FOURSQUARE = "https://api.foursquare.com/v2/";

    public static Retrofit retrofitAdapterFourSquare(){
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        okClientBuilder.addInterceptor(headerAuthorizationInterceptor);
        return new Retrofit.Builder()
                .baseUrl(URL_FOURSQUARE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static Interceptor headerAuthorizationInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Headers headers = request.headers().newBuilder()
                        .add(ACCEPT_LANGUAGE, TecnifiserApplication.getLanguage())
                        .build();
            request = request.newBuilder().headers(headers).build();
            return chain.proceed(request);
        }
    };

}
