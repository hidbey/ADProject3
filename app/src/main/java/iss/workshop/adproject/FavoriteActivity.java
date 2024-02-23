package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import iss.workshop.adproject.Adapters.BlogCardAdapterInF;
import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BlogDataService bDService;
    private List<Blog> blogs;
    private int activeUserId;
    private BlogCardAdapterInF adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        activeUserId = pref.getInt("user", 0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.193.162:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bDService = retrofit.create(BlogDataService.class);

        recyclerView = findViewById(R.id.rvFavouriteBlogs);

        Call<List<Blog>> call = bDService.getFavoriteBlogs(activeUserId);
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    blogs = response.body();
                    Log.e("1111111111111111", String.valueOf(blogs.size()));
                    recyclerView.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));
                    adapter = new BlogCardAdapterInF(blogs, FavoriteActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                Log.e("1111111111111111", blogs == null ? "null" : String.valueOf(blogs.size()));
            }
            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.e("3Retrofit", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteBlogs(); // 调用加载收藏博客的方法
    }

    private void loadFavoriteBlogs() {
        Call<List<Blog>> call = bDService.getFavoriteBlogs(activeUserId);
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    blogs = response.body();
                    // 检查adapter是否已经创建
                    if (adapter == null) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));
                        adapter = new BlogCardAdapterInF(blogs, FavoriteActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateBlogs(blogs);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
            }
        });
    }

}