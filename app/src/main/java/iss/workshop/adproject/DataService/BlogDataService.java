package iss.workshop.adproject.DataService;

import java.util.List;

import iss.workshop.adproject.Model.Blog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BlogDataService {

    @GET("/home/api/list")
    Call<List<Blog>> getAllBlogs();

    @POST("/home/api/create/{id}")
    Call<ResponseBody> createBlog(@Body Blog blog,@Path("id")int id);

    @GET("/home/api/create/history/{id}")
    Call<List<List<Blog>>> getBlogHistory(@Path("id")int id);

    @FormUrlEncoded
    @POST("/home/api/comment")
    Call<ResponseBody> createComment(@Field("commentContent") String commentContent,
                                     @Field("blogId") int blogId,
                                     @Field("userId") int userId);

    @GET("/home/api/getone/{blogId}")
    Call<Blog> getBlog(@Path("blogId") int blogId);

    @FormUrlEncoded
    @POST("/home/api/like")
    Call<ResponseBody> like(@Field("isliked") boolean isliked,
                            @Field("blogId") int blogId,
                            @Field("userId") int userId);

    @FormUrlEncoded
    @POST("/home/api/favorite")
    Call<ResponseBody> favorite(@Field("isfavorited") boolean isfavorited,
                                @Field("blogId") int blogId,
                                @Field("userId") int userId);
}
