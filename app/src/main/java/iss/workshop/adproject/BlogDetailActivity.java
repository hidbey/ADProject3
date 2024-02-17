package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import iss.workshop.adproject.Adapters.CommentAdapter;
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

public class BlogDetailActivity extends AppCompatActivity {

    private LinearLayout commentInputContainer; // TODO ↓
    private EditText commentEditText;
    private ImageButton btnComment;
    private Button sendCommentButton;
    private LinearLayout bottomActionBar;
    private TextView authorName;
    private ImageView authorAvatarImageView;
    private TextView publishTime;
    private int blogId;
    private Blog blog;
    private Blog blogInList; // TODO new new new
    private int position; // TODO new new new
    private List<Comment> comments;
    private CommentAdapter adapter;
    private RecyclerView rvComments;
    private BlogDataService bDService;// TODO ↑
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        // TODO ↓
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bDService = retrofit.create(BlogDataService.class);
        // TODO ↑

        // TODO 初始化视图组件 ↓
        commentInputContainer = findViewById(R.id.commentInputContainer);
        commentEditText = findViewById(R.id.commentEditText);
        btnComment = findViewById(R.id.btn_comment);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        bottomActionBar = findViewById(R.id.bottom_action_bar);
        authorName = findViewById(R.id.authorName);
        authorAvatarImageView = findViewById(R.id.authorAvatarImageView);
        publishTime = findViewById(R.id.publishTime);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView contentTextView = findViewById(R.id.contentTextView);
//        authorName.setText("author 1");
        // TODO 初始化视图组件 ↑

        // TODO get Blog
        Intent intent  = getIntent();
        blogId = intent.getIntExtra("blogId", 0);
        blogInList = (Blog) intent.getSerializableExtra("blogInList");
        from = intent.getStringExtra("from");

        position = intent.getIntExtra("position", 0);

        Call<Blog> call = bDService.getBlog(blogId);
        call.enqueue(new Callback<Blog>() {
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    blog = response.body();

                    Glide.with(BlogDetailActivity.this)
                            .load(blog.getBlogUser().getProfilePicture())
                            .circleCrop()
                            .into(authorAvatarImageView);

                    authorName.setText(blog.getBlogUser().getDisplayName());
                    titleTextView.setText(blog.getContentTitle());
                    contentTextView.setText(blog.getContent());
                    publishTime.setText("Posted on " + blog.getBlogTime());

                    rvComments = findViewById(R.id.rvComments);
                    rvComments.setLayoutManager(new LinearLayoutManager(BlogDetailActivity.this));

                    // TODO: 从服务器获取评论
                    comments = blog.getBlogComments();
                    adapter = new CommentAdapter(comments, BlogDetailActivity.this);
                    rvComments.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Blog> call, Throwable t) {

            }
        });

//        blogId = blog.getBlogId(); // TODO new

        ImageButton btnLike = findViewById(R.id.btn_like);
        btnLike.setSelected(isLiked()); // TODO new new 如果点赞过，进blog页面就点亮按钮
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());  // 切换选中状态
                // 点赞逻辑
                createLike();
            }
        });

        ImageButton btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setSelected(isFavorited()); // TODO new new 如果收藏过，进blog页面就点亮按钮
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());  // 切换选中状态
                // 收藏逻辑
                createFavorite();
            }
        });

        // TODO 设置评论按钮的点击事件
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentInputContainer.getVisibility() != View.VISIBLE) {
                    // 显示输入框和发送按钮
                    commentInputContainer.setVisibility(View.VISIBLE);
                    // 隐藏底部操作栏
                    bottomActionBar.setVisibility(View.GONE);
                    // 请求输入框的焦点
                    commentEditText.requestFocus();
                    // 弹出键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(commentEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        // TODO 设置发送评论按钮的点击事件
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                if (!commentText.isEmpty()) {
                    // TODO: 发送评论到服务器
                    sendCommentToServer(commentText);
                    commentEditText.setText(""); // 清空输入框
                    commentInputContainer.setVisibility(View.GONE); // 隐藏输入界面
                    // 显示底部操作栏
                    bottomActionBar.setVisibility(View.VISIBLE);
                    // 隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
                }
            }
        });

        View.OnClickListener authorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlogDetailActivity.this, UserDetailActivity.class);
                intent.putExtra("userId", blog.getBlogUser().getUserId());
                startActivity(intent);
            }
        };
        // TODO 点击作者名和头像
        authorName.setOnClickListener(authorClickListener);
        authorAvatarImageView.setOnClickListener(authorClickListener);

    }

    // TODO new ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private void sendCommentToServer(String commentText) {
        // 发送评论到服务器的代码...
        // blogId, userId, commentContent
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        int userId = pref.getInt("user", 0);

        Call<ResponseBody> call = bDService.createComment(commentText, blogId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()&&response.body()!=null) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("BlogDetailActivity.sendCommentToServer", "Error: " + t.getMessage());
            }
        });

        Comment comment = new Comment();
        comment.setCommentContent(commentText);
        BlogUser user = new BlogUser();
        user.setUserId(userId);
        user.setDisplayName(pref.getString("userName", "user"));
        user.setProfilePicture(pref.getString("headPicture", null));
        comment.setCommentBlogUser(user);
        comments.add(comment);
        adapter.notifyDataSetChanged();

        blogInList.setBlogCommentCount(blogInList.getBlogCommentCount() + 1); // TODO blogInList
        sendMessageToFragment();

        NestedScrollView nestedScrollView = findViewById(R.id.nsv);

        Handler handler = new Handler(Looper.getMainLooper());

// 使用 postDelayed 方法延迟执行滚动操作
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 执行滚动操作
                nestedScrollView.smoothScrollTo(0, nestedScrollView.getChildAt(0).getBottom());
            }
        }, 500); // 延迟时间设置为0.5s
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 隐藏键盘
        if (commentInputContainer.getVisibility() == View.VISIBLE) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        // 如果输入框是可见的，则隐藏它并且隐藏键盘
        if (commentInputContainer.getVisibility() == View.VISIBLE) {
            commentInputContainer.setVisibility(View.GONE);
            // 显示底部操作栏
            bottomActionBar.setVisibility(View.VISIBLE);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);

                Rect sendButtonRect = new Rect();
                sendCommentButton.getGlobalVisibleRect(sendButtonRect);

                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())
                        && !sendButtonRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    hideKeyboard(v);
                    // 隐藏评论输入框
                    commentInputContainer.setVisibility(View.GONE);
                    // 显示底部操作栏
                    bottomActionBar.setVisibility(View.VISIBLE);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // 是否点赞过
    private Boolean isLiked() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String likeList = pref.getString("likeList", "");
        return likeList.contains(String.valueOf(blogId));
    }

    // 是否收藏
    private Boolean isFavorited() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String favoriteList = pref.getString("favoriteList", "");
        return favoriteList.contains(String.valueOf(blogId));
    }

    private void createLike() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int userId = pref.getInt("user", 0);

        Call<ResponseBody> call = bDService.like(isLiked(), blogId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()&&response.body()!=null) {}
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("BlogDetailActivity.sendCommentToServer", "Error: " + t.getMessage());
            }
        });

        // TODO SharedPreferences更新点赞列表
        String likeList = pref.getString("likeList", "");
        if(isLiked()) { // 点过赞
            List<String> likedBlogIdList = new ArrayList<>(Arrays.asList(likeList.split(",")));
            likedBlogIdList.remove(String.valueOf(blogId));
            String updatedLikedBlogIds = String.join(",", likedBlogIdList);
            editor.putString("likeList", updatedLikedBlogIds);

            blogInList.setBlogLikeCount(blogInList.getBlogLikeCount() - 1); // TODO blogInList
            sendMessageToFragment();
        } else {
            editor.putString("likeList", likeList + "," + blogId);

            blogInList.setBlogLikeCount(blogInList.getBlogLikeCount() + 1); // TODO blogInList
            sendMessageToFragment();
        }
        editor.commit();
    }

    private void createFavorite() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int userId = pref.getInt("user", 0);

        Call<ResponseBody> call = bDService.favorite(isFavorited(), blogId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()&&response.body()!=null) {}
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("BlogDetailActivity.sendCommentToServer", "Error: " + t.getMessage());
            }
        });

        // TODO SharedPreferences更新收藏列表
        String favoriteList = pref.getString("favoriteList", "");
        if(isFavorited()) { // 点过赞
            List<String> favouritedBlogIdList = new ArrayList<>(Arrays.asList(favoriteList.split(",")));
            favouritedBlogIdList.remove(String.valueOf(blogId));
            String updatedFavouritedBlogIds = String.join(",", favouritedBlogIdList);
            editor.putString("favoriteList", updatedFavouritedBlogIds);
        } else {
            editor.putString("favoriteList", favoriteList + "," + blogId);
        }
        editor.commit();
    }

    private void sendMessageToFragment() {
        if(from.equals("home")) {
            Intent intent = new Intent("countChange");
            intent.putExtra("message", position + "," + blogInList.getBlogLikeCount() + "," + blogInList.getBlogCommentCount());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else if (from.equals("user")) {
            Intent intent = new Intent("countChangeInUda");
            intent.putExtra("message", position + "," + blogInList.getBlogLikeCount() + "," + blogInList.getBlogCommentCount());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }


    // TODO new ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}