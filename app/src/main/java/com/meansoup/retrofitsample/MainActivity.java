package com.meansoup.retrofitsample;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity
{
    private TextView mRepoText;
    private Button mGetButton;
    private Button mPostButton;

    private Retrofit retrofit;
    private GitHubService gitHubService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRepoText = (TextView) findViewById(R.id.repoText);
        mGetButton = (Button) findViewById(R.id.getButton);
        mPostButton = (Button) findViewById(R.id.postButton);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubService = retrofit.create(GitHubService.class);

        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatas();
            }
        });


        //post dosen't work. because these url under the code isn't work
        //if you want to use change appropriately for your server
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDatas(new User("guy", "https://www.github.com/meansoup"));
            }
        });

    }

    public void postDatas(User user)
    {
        Call<User> call = gitHubService.postRepos("meansoup", user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), "change url appropriately", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    String str = "response code: " + response.code() + "\n ID: " + response.body().login + "\n URL: " +  response.body().html_url;
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t){
                Log.e("Not Response", t.getLocalizedMessage());
            }
        });
    }

    public void getDatas()
    {

        Call<User> call = gitHubService.getRepos("meansoup");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String str = "response code: " + response.code() + "\n ID: " + response.body().login + "\n URL: " +  response.body().html_url;
                    mRepoText.setText(str);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t){
                Log.e("Not Response", t.getLocalizedMessage());
            }
        });
    }

}