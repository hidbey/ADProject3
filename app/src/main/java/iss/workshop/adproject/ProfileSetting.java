package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.126:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        id  = pref.getInt("user",-1);
        uDService = retrofit.create(UserDataService.class);
        recyclerView = findViewById(R.id.recyclerViewProfileCards);
        findBlogUserById(id);


    }


    public void findBlogUserById(int id){
        Call<BlogUser> call = uDService.getBloguserById(id);
        call.enqueue(new Callback<BlogUser>() {
            @Override
            public void onResponse(Call<BlogUser> call, Response<BlogUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    findedUser = response.body();
                    profileSettingAdapter = new ProfileSettingAdapter(findedUser);
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