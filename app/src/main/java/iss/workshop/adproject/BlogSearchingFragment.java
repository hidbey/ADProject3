package iss.workshop.adproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.Adapters.BlogCardAdapter;
import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogSearchingFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BlogDataService bDService;
    private List<Blog> titles = new ArrayList<>();
    private BlogCardAdapter adapter;

    public BlogSearchingFragment() {
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//在这里面初始化组件
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_searching, container, false);

        recyclerView = view.findViewById(R.id.recycler_seraching_result_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.126:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bDService = retrofit.create(BlogDataService.class);
        adapter = new BlogCardAdapter(titles, getActivity());

        //toolbar.setTitle("Search");

        searchView = view.findViewById(R.id.search_view);

        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.BLACK); // 设置搜索框中输入文本的颜色为黑色
        searchAutoComplete.setHintTextColor(Color.GRAY); // 可以同时设置提示文本的颜色为灰色

        // 设置SearchView的查询文本监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getAllBlogs();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                searchView.clearFocus();
                // 触发AppBarLayout折叠
                AppBarLayout appBarLayout = getView().findViewById(R.id.appBarLayout);
                appBarLayout.setExpanded(false, true);



                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 查询文本发生变化
                return false;
            }
        });

        return view;
    }

    public void getAllBlogs(){
        Call<List<Blog>> call = bDService.getAllBlogs();
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    titles = response.body(); // 清空现有数据
                    adapter.setBlogs(titles); // 添加新数据
                    recyclerView.setAdapter(adapter); // 通知数据变化
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.e("3Retrofit", "Error: " + t.getMessage());
            }


        });
    }


}