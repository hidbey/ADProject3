package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import iss.workshop.adproject.Adapters.ProfileSettingAdapter;
import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.BlogUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileSetting extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileSettingAdapter profileSettingAdapter;
    int id;
    UserDataService uDService;
    BlogUser findedUser;
    String profilePicture;
    ImageView imageView;
    Button savaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        id  = pref.getInt("user",-1);
        profilePicture = pref.getString("headPicture",null);
        imageView = findViewById(R.id.imageViewProfile);
        Glide.with(this)
                .load(profilePicture)
                .circleCrop()
                .into(imageView);

        savaButton = findViewById(R.id.buttonSaveProfile);
        uDService = retrofit.create(UserDataService.class);
        recyclerView = findViewById(R.id.recyclerViewProfileCards);
        findBlogUserById(id);

        savaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileSettingAdapter adapter = (ProfileSettingAdapter) recyclerView.getAdapter();
                adapter.saveUpdatedUser();
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    public void findBlogUserById(int id){
        Call<BlogUser> call = uDService.getBloguserById(id);
        call.enqueue(new Callback<BlogUser>() {
            @Override
            public void onResponse(Call<BlogUser> call, Response<BlogUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    findedUser = response.body();
                    profileSettingAdapter = new ProfileSettingAdapter(findedUser,getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(ProfileSetting.this));
                    recyclerView.setAdapter(profileSettingAdapter);
                }
            }

            @Override
            public void onFailure(Call<BlogUser> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage(), t);
            }
        });
    }
}