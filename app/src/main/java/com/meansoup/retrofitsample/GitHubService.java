package com.meansoup.retrofitsample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/users/{ID}")
    Call<User> getRepos(@Path("ID") String id);

    @POST("/users/{ID}")
    Call<User> PostRepos(@Path("ID") String id);
}
