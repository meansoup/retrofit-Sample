package com.meansoup.retrofitsample;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity
{
    TextView mRepoText;
    Button mGetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRepoText = (TextView) findViewById(R.id.repoText);
        mGetButton = (Button) findViewById(R.id.getButton);

        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatas();
            }
        });

    }// End OnCreate

    public void getDatas()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService gitHubService = retrofit.create(GitHubService.class);

        Call<User> call = gitHubService.getRepos("meansoup");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                String str = "response code: " + response.code() + ", ID: " + response.body().login + ", URL: " +  response.body().html_url;
                Log.d("#RESPONSE CHECK", str);

                if (response.isSuccessful() && response.body() != null) {
                    mRepoText.setText(response.body().login + ": " +response.body().html_url);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t){
                Log.e("Not Response", t.getLocalizedMessage());
            }
        });
    }

}