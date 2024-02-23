package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogHistory;
import iss.workshop.adproject.Model.BlogUser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateProfile extends AppCompatActivity {
    //TextInputLayout updateDisplayNameLayout,updateEmailLayout,updateProfileTaglineLayout,updateLocationLayout,updateAboutmeLayout;
    TextInputEditText updateDisplayName,updateEmail, updateProfileTagline, updateLocation, updateAboutme,updateMyTechStack,updateGithub,updateLinkedin;
    private TextInputLayout emailLayout,githubLayout,lindedinLayout;
    private UserDataService uDService;
    String name;
    int id;
    BlogUser findedUser = new BlogUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initText();

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.193.162:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        id  = pref.getInt("user",-1);
        uDService = retrofit.create(UserDataService.class);


        findBlogUserById(id);

        updateEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = s.toString().trim();
                validEmail(emailInput);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateGithub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String githubInput = s.toString().trim();
                validGithubLink(githubInput);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateLinkedin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String linkedinInput = s.toString().trim();
                validLinkedinLink(linkedinInput);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void initText(){
        updateAboutme = findViewById(R.id.updateDisplayName);
        updateEmail= findViewById(R.id.updateEmail);
        updateProfileTagline =findViewById(R.id.updateProfileTagline);
        updateLocation = findViewById(R.id.updateLocation);
        updateDisplayName = findViewById(R.id.updateDisplayName);
        updateMyTechStack = findViewById(R.id.updateMyTechStack);
        updateGithub = findViewById(R.id.updateGithub);
        updateLinkedin = findViewById(R.id.updateLinkedin);
        emailLayout = findViewById(R.id.updateEmailLayout);
        githubLayout = findViewById(R.id.updateGithubLayout);
        lindedinLayout = findViewById(R.id.updateLinkedinLayout);
    }

    public void findBlogUserById(int id){
        Call<BlogUser> call = uDService.getBloguserById(id);
        call.enqueue(new Callback<BlogUser>() {
            @Override
            public void onResponse(Call<BlogUser> call, Response<BlogUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    findedUser = response.body();

                    updateEmail.setText(findedUser.getEmail());
                    updateAboutme.setText(findedUser.getAboutMe());
                    updateLocation.setText(findedUser.getLocation());
                    updateMyTechStack.setText(findedUser.getMyTechStack());
                    updateDisplayName.setText(findedUser.getDisplayName());
                    updateProfileTagline.setText(findedUser.getProfileTagline());
                    updateGithub.setText(findedUser.getGithubLink());
                    updateLinkedin.setText(findedUser.getLinkedinLink());
                }
            }

            @Override
            public void onFailure(Call<BlogUser> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage(), t);
            }
        });
    }



    private void validEmail(CharSequence target) {//验证邮箱格式
        boolean trueOfFalse = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        if (!trueOfFalse){
            emailLayout.setError("Invalid email address");
        }else
            emailLayout.setError(null);
    }

    private void validGithubLink(String url){
        String urlPattern = "^(https?|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:[0-9]{2,})?(/[a-zA-Z0-9-_.~!*:@&+\\/=]*)?$";
        Pattern pattern = Pattern.compile(urlPattern);

        if (!pattern.matcher(url).matches()){
            githubLayout.setError("The provided link does not adhere to the specified format");
        }else {
            githubLayout.setError(null);
        }
    }

    private void validLinkedinLink(String url){
        String urlPattern = "^(https?|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:[0-9]{2,})?(/[a-zA-Z0-9-_.~!*:@&+\\/=]*)?$";
        Pattern pattern = Pattern.compile(urlPattern);

        if (!pattern.matcher(url).matches()){
            lindedinLayout.setError("The provided link does not adhere to the specified format");
        }else {
            lindedinLayout.setError(null);
        }
    }

    public void onSaveClick(View view) {
        if (checkEnableRegisterButton()){
            BlogUser simplUser = new BlogUser();
            simplUser.setDisplayName(updateDisplayName.getText().toString());
            simplUser.setLocation(updateLocation.getText().toString());
            simplUser.setEmail(updateEmail.getText().toString());
            simplUser.setAboutMe(updateAboutme.getText().toString());
            simplUser.setProfileTagline(updateProfileTagline.getText().toString());
            simplUser.setLinkedinLink(updateLinkedin.getText().toString());
            simplUser.setGithubLink(updateGithub.getText().toString());
            simplUser.setMyTechStack(updateMyTechStack.getText().toString());

            saveUserInfo(simplUser);
        }else {
            Toast toast = Toast.makeText(this,"inputs have error",Toast.LENGTH_SHORT);
        }

    }

    private void saveUserInfo(BlogUser user) {
        Call<ResponseBody> call =uDService.updateUser(id,user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userName",updateDisplayName.getText().toString());
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Log.d("Retrofit", "Response Body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void inHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean checkEnableRegisterButton() {
        return emailLayout.getError()==null&&githubLayout.getError()==null&&lindedinLayout.getError()==null;
    }


}