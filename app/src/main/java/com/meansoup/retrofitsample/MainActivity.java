package com.meansoup.retrofitsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {
    private TextView mRepoText;
    private Button mGetButton;
    private Button mPostButton;
    private Button mImageButton;

    private Retrofit retrofit;
    private GitHubService gitHubService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRepoText = (TextView) findViewById(R.id.repoText);
        mGetButton = (Button) findViewById(R.id.getButton);
        mPostButton = (Button) findViewById(R.id.postButton);
        mImageButton = (Button) findViewById(R.id.ImageButton);

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

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                  //post dosen't work. because these url under the code isn't work
                postDatas(new User("11", "22"));                    //if you want to use change appropriately for your server
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile("/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-05-18-16-14-09.png");  // your file path must be here
            }
        });

    }

    public void postDatas(User user) {
        Call<User> call = gitHubService.postRepos("meansoup", user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), "change url appropriately", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    String str = "response code: " + response.code() + "\n ID: " + response.body().login + "\n URL: " + response.body().html_url;
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Not Response", t.getLocalizedMessage());
            }
        });
    }

    public void getDatas() {

        Call<User> call = gitHubService.getRepos("meansoup");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String str = "response code: " + response.code() + "\n ID: " + response.body().login + "\n URL: " + response.body().html_url;
                    mRepoText.setText(str);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Not Response", t.getLocalizedMessage());
            }
        });
    }

    public void uploadFile(String filePath) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        FileUploadService service = new Retrofit.Builder().baseUrl("https://api.github.com").client(client).build().create(FileUploadService.class);

        File file = new File(filePath);         //Log.d(filePath, file.toString());

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}