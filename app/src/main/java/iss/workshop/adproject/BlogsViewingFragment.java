package iss.workshop.adproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.Adapters.BlogCardAdapter;
import iss.workshop.adproject.Adapters.TitleAdapter;
import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogsViewingFragment extends Fragment {
    private RecyclerView recyclerView;
    private BlogCardAdapter adapter;
    private BlogDataService bDService;
    private List<Blog> titles = new ArrayList<>();
    private int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter("countChange");
        broadcastManager.registerReceiver(mMessageReceiver, intentFilter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取消息
            String message = intent.getStringExtra("message");
            // 处理消息
            String[] msg = message.split(",");
            pos = Integer.parseInt(msg[0]);
            titles.get(pos).setBlogLikeCount(Integer.parseInt(msg[1]));
            titles.get(pos).setBlogCommentCount(Integer.parseInt(msg[2]));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false);
        View view = inflater.inflate(R.layout.fragment_blogs_viewing, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bDService = retrofit.create(BlogDataService.class);
        adapter = new BlogCardAdapter(titles, getActivity());
        getTitles();
        return view;
    }

    private void getTitles() {
        // TODO: 返回标题列表数据，例如从数据库或服务器获取
        Call<List<Blog>> call = bDService.getAllBlogs();
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    titles = response.body();
                    adapter.setBlogs(titles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.e("3Retrofit", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 检查数据变化并刷新 Adapter
        adapter.notifyItemChanged(pos);
    }
}