package iss.workshop.adproject.Model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.R;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogHistoryViewModel extends ViewModel {//集成一下，这边只负责数据的处理
    private final MutableLiveData<List<BlogGroup>> blogGroupsLiveData = new MutableLiveData<>();
    private BlogDataService bDService;
    List<List<Blog>>titles;

    private Context context;


    public LiveData<List<BlogGroup>> getBlogGroups() {
        return blogGroupsLiveData;
    }

    public void setContext(Context context){
        this.context = context;

        initRetrofit();
    }


    public BlogHistoryViewModel() {
        // 初始化Retrofit和bDService

    }

    public void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.193.162:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bDService = retrofit.create(BlogDataService.class);
    }

    public void loadBlogs(int id) {
        Call<List<List<Blog>>> call = bDService.getBlogHistory(id);
        call.enqueue(new Callback<List<List<Blog>>>() {
            @Override
            public void onResponse(Call<List<List<Blog>>> call, Response<List<List<Blog>>> response) {
                if (response.isSuccessful()&&response.body()!=null) {
                    List<BlogGroup>groups = new ArrayList<>();
                    titles = response.body();
                    BlogGroup blogGroup = new BlogGroup();
                    blogGroup.setGroupName("today");
                    blogGroup.setExpanded(true);
                    blogGroup.setBlogs(titles.get(0));
                    groups.add(blogGroup);
                    BlogGroup blogGroup1 = new BlogGroup();
                    blogGroup1.setGroupName("in 3 days");
                    blogGroup1.setBlogs(titles.get(1));
                    BlogGroup blogGroup2 = new BlogGroup();
                    blogGroup2.setBlogs(titles.get(2));
                    blogGroup2.setGroupName("in 7 days");
                    BlogGroup blogGroup3 = new BlogGroup();
                    blogGroup3.setGroupName("longer");
                    blogGroup3.setBlogs(titles.get(3));
                    groups.add(blogGroup1);
                    groups.add(blogGroup2);
                    groups.add(blogGroup3);
                    blogGroupsLiveData.setValue(groups);
                }
            }

            @Override
            public void onFailure(Call<List<List<Blog>>> call, Throwable t) {
            }
        });
    }
}