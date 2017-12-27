package com.wwb.bill.lab9.factory;


import com.wwb.bill.lab9.model.Github;
import com.wwb.bill.lab9.model.Repos;
import com.wwb.bill.lab9.service.GithubService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by 15945 on 2017/12/22.
 */

public class ServiceFactory {
    private final String url = "https://api.github.com/";
    private static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .readTimeout(30,TimeUnit.SECONDS) //读超时
                .readTimeout(10,TimeUnit.SECONDS) //写超时
                .build();
        return okHttpClient;
    }

    private static Retrofit createRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }

    public Observable<Github> getUser(String user) {
        GithubService service= createRetrofit(url).create(GithubService.class);
        return service.getUser(user);
    }

    public Observable<List<Repos>> getRepos(String user) {
        GithubService service= createRetrofit(url).create(GithubService.class);
        return service.getRepos(user);
    }
}
