package iss.workshop.adproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import iss.workshop.adproject.Adapters.CommentAdapter;
import iss.workshop.adproject.Adapters.TitleAdapter;
import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogUser;
import iss.workshop.adproject.Model.Comment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetailActivity extends AppCompatActivity {
    private boolean isFollowed;
    private UserDataService uDService;
    private BlogUser user;
    private ImageView userAvatarImageView;
    private TextView usernameTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private Button followButton;
    private RecyclerView blogsRecyclerView;
    private int activeUserId;
    private int followers;
    private List<Blog> blogs;
    private TitleAdapter adapter;
    private int pos;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取消息
            String message = intent.getStringExtra("message");
            // 处理消息
            String[] msg = message.split(",");
            pos = Integer.parseInt(msg[0]);
            blogs.get(pos).setBlogLikeCount(Integer.parseInt(msg[1]));
            blogs.get(pos).setBlogCommentCount(Integer.parseInt(msg[2]));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 检查数据变化并刷新 Adapter
        if(adapter != null) {
            adapter.notifyItemChanged(pos);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter("countChangeInUda");
        broadcastManager.registerReceiver(mMessageReceiver, intentFilter);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        activeUserId = pref.getInt("user", 0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.126:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uDService = retrofit.create(UserDataService.class);

        userAvatarImageView = findViewById(R.id.userAvatarImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        followingCountTextView = findViewById(R.id.followingCountTextView);
        followersCountTextView = findViewById(R.id.followersCountTextView);
        followButton = findViewById(R.id.followButton);
        blogsRecyclerView = findViewById(R.id.blogsRecyclerView);

        // 接收传递的作者信息
        int userId = getIntent().getIntExtra("userId", 0);
        // TODO 去获取用户信息
        Call<BlogUser> call = uDService.getBloguserById(userId);
        call.enqueue(new Callback<BlogUser>() {
            @Override
            public void onResponse(Call<BlogUser> call, Response<BlogUser> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    user = response.body();

                    checkFollowStatus();
                    // TODO -----------------------------
                    usernameTextView.setText(user.getDisplayName());
                    followingCountTextView.setText("following: " + String.valueOf(user.getFollowings().size()));
                    followersCountTextView.setText("follower: " + String.valueOf(user.getFollower().size()));
                    followers = user.getFollower().size();
                    Glide.with(UserDetailActivity.this)
                            .load(user.getProfilePicture())
                            .circleCrop()
                            .into(userAvatarImageView);
                    blogsRecyclerView.setLayoutManager(new LinearLayoutManager(UserDetailActivity.this));
                    blogs = user.getPostedBlogs();
                    adapter = new TitleAdapter(blogs, UserDetailActivity.this);
                    blogsRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<BlogUser> call, Throwable t) {

            }
        });

        // TODO button 检查是否关注
//        checkFollowStatus(); 移到user = response.body(); （call.enqueue里）

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现关注逻辑，例如更新数据库、发送网络请求等

                isFollowed = !isFollowed; // 切换关注状态
                if (isFollowed) {
                    // 执行关注操作的逻辑
                    followersCountTextView.setText("follower: " + String.valueOf(++followers));
                    Toast.makeText(UserDetailActivity.this, "Followed " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    // 执行取消关注操作的逻辑
                    followersCountTextView.setText("follower: " + String.valueOf(--followers));
                    Toast.makeText(UserDetailActivity.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                }
                Call<ResponseBody> call = uDService.follow(userId, activeUserId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()&&response.body()!=null) {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("UserDetailActivity.followButton.setOnClickListener", "Error: " + t.getMessage());
                    }
                });
                setFollowButtonState();
            }
        });
    }

    private void checkFollowStatus() {
        if (activeUserId == user.getUserId()) {
            followButton.setVisibility(View.GONE);
            return;
        }
        isFollowed = false;
        // 检查是否已关注作者
        for (BlogUser u : user.getFollower()) {
            if (u.getUserId() == activeUserId) {
                isFollowed = true;
                break;
            }
        }
        setFollowButtonState();
    }

    private void setFollowButtonState() {
        if (isFollowed) {
            // 设置为已关注状态
            followButton.setText("following");
            followButton.setBackgroundColor(Color.GRAY); // 示例颜色，根据您的设计调整
        } else {
            // 设置为关注状态
            followButton.setText("follow");
            followButton.setBackgroundColor(Color.BLUE); // 示例颜色，根据您的设计调整
        }
    }
}