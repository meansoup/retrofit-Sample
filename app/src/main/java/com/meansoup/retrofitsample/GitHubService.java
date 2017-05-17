package com.meansoup.retrofitsample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alsrn on 2017-05-17.
 */

public interface GitHubService {
    @GET("/users/{ID}")
    Call<User> getRepos(@Path("ID") String id);
}
