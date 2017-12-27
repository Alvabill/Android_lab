package com.wwb.bill.lab9.service;

import com.wwb.bill.lab9.model.Github;
import com.wwb.bill.lab9.model.Repos;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 15945 on 2017/12/22.
 */

public interface GithubService {
    @GET("/users/{user}")
    Observable<Github> getUser(
            @Path("user") String user
    );

    @GET("/users/{user}/repos")
    Observable<List<Repos>> getRepos(@Path("user") String user);
}
