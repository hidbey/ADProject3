package iss.workshop.adproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iss.workshop.adproject.Adapters.FollowingUsersAdapter;
import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowingUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FollowingUsersAdapter adapter;
    private List<BlogUser> followingUsers;
    private int activeUserId;
    private BlogUser activeUser;
    private UserDataService uDService;
    TextView tvNoFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_users);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        activeUserId = pref.getInt("user", 0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.193.162:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uDService = retrofit.create(UserDataService.class);
        tvNoFollowing = findViewById(R.id.tvNoFollowing);
        recyclerView = findViewById(R.id.rvFollowingUsers);


        Call<BlogUser> call = uDService.getBloguserById(activeUserId);
        call.enqueue(new Callback<BlogUser>() {
            @Override
            public void onResponse(Call<BlogUser> call, Response<BlogUser> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    activeUser = response.body();

                    followingUsers = activeUser.getFollowings();
                    Log.e("size",followingUsers.size()+"");
                    if (followingUsers.size()==0){
                        tvNoFollowing.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        tvNoFollowing.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FollowingUsersActivity.this));
                        adapter = new FollowingUsersAdapter(followingUsers, FollowingUsersActivity.this);
                        recyclerView.setAdapter(adapter);
                    }


                }

            }
            @Override
            public void onFailure(Call<BlogUser> call, Throwable t) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFollowing(); // 调用加载收藏博客的方法
    }

    private void loadFollowing() {
        Call<BlogUser> call = uDService.getBloguserById(activeUserId);
        call.enqueue(new Callback<BlogUser>() {
            @Override
            public void onResponse(Call<BlogUser> call, Response<BlogUser> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    activeUser = response.body();

                    followingUsers = activeUser.getFollowings();
                    Log.e("size",followingUsers.size()+"");
                    if (followingUsers.size()==0){
                        tvNoFollowing.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        tvNoFollowing.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FollowingUsersActivity.this));
                        adapter = new FollowingUsersAdapter(followingUsers, FollowingUsersActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<BlogUser> call, Throwable t) {
            }
        });
    }
}