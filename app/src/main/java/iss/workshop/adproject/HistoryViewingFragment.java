package iss.workshop.adproject;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.Adapters.ExpandableGroupAdapter;

import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogGroup;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpandableGroupAdapter adapter;
    private BlogDataService bDService;
    private List<Blog> titles = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_viewing, container, false);
        recyclerView = view.findViewById(R.id.recyclerHistoryView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ExpandableGroupAdapter(getActivity(),new ArrayList<>());
        recyclerView.setAdapter(adapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bDService = retrofit.create(BlogDataService.class);
        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        int id = pref.getInt("user",-1);
        getTitles(id);

//        adapter.setOnItemClickListener(position -> {
//            // 处理点击事件
//            String title = getTitles().get(position);
//            Toast.makeText(getActivity(), "Clicked on: " + title, Toast.LENGTH_SHORT).show();
//        });
    return view;
    }

    private void getTitles(int id) {
        // TODO: 返回标题列表数据，例如从数据库或服务器获取

        Call<List<Blog>> call = bDService.getBlogHistory(id);
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    List<BlogGroup>groups = new ArrayList<>();
                    titles = response.body();
                    BlogGroup blogGroup = new BlogGroup();
                    blogGroup.setGroupName("today");
                    blogGroup.setExpanded(true);
                    blogGroup.setBlogs(titles);
                    groups.add(blogGroup);
                    adapter = new ExpandableGroupAdapter(getActivity(),groups);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
            }
        });
    }
}