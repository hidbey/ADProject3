package iss.workshop.adproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.Adapters.TitleAdapter;
import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryViewingFragment extends Fragment {

    private RecyclerView recyclerView;
    private TitleAdapter adapter;
    private BlogDataService bDService;
    private List<Blog> titles = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        getTitles();


//        adapter.setOnItemClickListener(position -> {
//            // 处理点击事件
//            String title = getTitles().get(position);
//            Toast.makeText(getActivity(), "Clicked on: " + title, Toast.LENGTH_SHORT).show();
//        });





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
                    adapter = new TitleAdapter(titles, getActivity());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.e("3Retrofit", "Error: " + t.getMessage());
            }
        });
    }
}