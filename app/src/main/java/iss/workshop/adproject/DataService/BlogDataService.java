package iss.workshop.adproject.DataService;

import java.util.List;

import iss.workshop.adproject.Model.Blog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BlogDataService {

    @GET("/home/api/list")
    Call<List<Blog>> getAllBlogs();

    @POST("/home/api/create")
    Call<ResponseBody> createBlog(@Body Blog blog);

    @GET("/home/api/create/history/{id}")
    Call<List<List<Blog>>> getBlogHistory(@Path("id")int id);
}
