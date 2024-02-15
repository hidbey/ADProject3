package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogStatusEnum;
import iss.workshop.adproject.Model.BlogUser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogTypeActivity extends AppCompatActivity {
    String contentTitle,subTitle,content;
    Button button;
    ChipGroup coverChipGroup,techChipGroup;
    List<String> techList = new ArrayList<>();
    Blog blog = new Blog();
    int id;
    UserDataService uDService;
    BlogDataService bDService;
    BlogUser currentUser = new BlogUser();
    TextView contentTitleTextView;
    private static final int RESULT_LOAD_IMAGE = 1;
    Toolbar toolbar;
    ImageView imageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_type);


        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        id  = pref.getInt("user",-1);
        uDService = retrofit.create(UserDataService.class);
        bDService = retrofit.create(BlogDataService.class);
        initIntent();
        initComponents();
        initChipGroup();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行返回操作，例如调用 finish() 方法关闭当前 Activity
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //遍历技术chip查看是否被选中，然后去添加到list
                for (int i = 0; i < techChipGroup.getChildCount(); i++) {
                    View childView = techChipGroup.getChildAt(i);

                    if (childView instanceof Chip) {
                        Chip chip = (Chip) childView;

                        if (chip.isChecked()) {
                            techList.add(chip.getText().toString());
                        }
                    }
                }
                List<String>languages = new ArrayList<>();
                Collections.addAll(languages,"Chinese","English");

                blog.setBlogTime(getPostTime());
                currentUser.setDisplayName("ioioioio");
                blog.setBlogUser(currentUser);

                blog.setBlogCommentCount(0);
                blog.setBlogLikeCount(0);
                blog.setContent(content);
                blog.setContentTitle(contentTitle);
                blog.setSubTitle(subTitle);
                blog.setImage(null);
                blog.setTechniqueSelected(techList);
                blog.setLanguageSelected(languages);
                blog.setBlogStatus(BlogStatusEnum.POSTED);
                blog.setReadingTime(0);//后面要改
                blog.setBlogComments(new ArrayList<>());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(blog);
                Log.d("Retrofit", "Request Body: " + json);

                Call<ResponseBody> call = bDService.createBlog(blog,id);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            Log.d("Retrofit", "Successfully post");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("FailureRetrofit", "Error: " + t.getMessage());
                    }
                });
            }
        });

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    protected void initComponents(){
        button = findViewById(R.id.button);
        contentTitleTextView = findViewById(R.id.content_title);
        contentTitleTextView.setText(contentTitle);
        imageUpload = findViewById(R.id.image_upload);
        toolbar  = findViewById(R.id.toolbar);
    }

    protected void initIntent(){
        Intent intent = getIntent();
        contentTitle = intent.getStringExtra("contentTitle");
        subTitle = intent.getStringExtra("subTitle");
        content = intent.getStringExtra("content");
    }

    protected void initChipGroup(){
        coverChipGroup = findViewById(R.id.cover_chip_group);
        coverChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                ImageView imageUpload = findViewById(R.id.image_upload);

                if (checkedId == R.id.chip_single_image) {
                    //如果选择了“单图”，设置 TextView 宽度为 wrap_content
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentTitleTextView.getLayoutParams();
                    layoutParams.width = 850; //或者 getResources().getDimensionPixelSize(R.dimen.your_dimension)
//应用修改后的LayoutParams
                    contentTitleTextView.setLayoutParams(layoutParams);
                    // 显示 ImageView
                    imageUpload.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.chip_no_cover) {
                    // 如果选择了“无封面”，设置 TextView 宽度为 match_parent
                    contentTitleTextView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    contentTitleTextView.requestLayout();

                    // 隐藏 ImageView
                    imageUpload.setVisibility(View.GONE);
                }
            }
        });

        techChipGroup = findViewById(R.id.tech_chip_group);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }


    public String getPostTime() {
        LocalDate createdTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = createdTime.format(formatter);

        return formattedDate;
    }

}