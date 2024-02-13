package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.widget.Button;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.BUserStatusEnum;
import iss.workshop.adproject.Model.BlogUser;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signUp extends AppCompatActivity {
    private TextInputLayout usernameLayout, passwordLayout, emailLayout, secondpasswordLayout;
    private TextInputEditText usernameEditText, passwordEditText, secondpasswordEditText, emailEditText;
    private Button button;
    UserDataService uDService;
    String username, email,finalPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                //.client(okHttpClient)
                .build();

        uDService = retrofit.create(UserDataService.class);


        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        secondpasswordLayout = findViewById(R.id.SecondpasswordLayout);

        secondpasswordEditText = findViewById(R.id.SecondpasswordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        button = findViewById(R.id.registerButton);

        button.setEnabled(false);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateUsername(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
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

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                finalPassword = editable.toString().trim();
            }
        });

        secondpasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateSecondPassword(s.toString(),finalPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(view -> {
            //在这里执行注册逻辑
            username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            email = emailEditText.getText().toString();

            BlogUser user = new BlogUser();
            initUser(username,password,email,user);

            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(user);
            Log.d("1Retrofit", "Request Body: " + json);
            Call<ResponseBody> call = uDService.createUser(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.body()!=null){
                        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username",username);
                        editor.putString("email",email);
                        editor.putString("myTechStack",user.getMyTechStack());
                        editor.putString("profileTagline",user.getProfileTagline());
                        editor.putString("aboutMe",user.getAboutMe());
                        editor.putString("location",user.getLocation());
                        editor.putString("githubLink",user.getGithubLink());
                        editor.putString("linkedinLink",user.getLinkedinLink());
                        inHomePage();
                    }else{
                        Log.d("2Retrofit", "Response Body: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("3Retrofit", "Error: " + t.getMessage());
                }
            });
        });
    }

    private void validateUsername(String username) {
        if (username.length() > 11) {
            usernameLayout.setError("Username must be 11 characters or less");
            button.setEnabled(false);
        } else {
            usernameLayout.setError(null);
            checkEnableRegisterButton();
        }
    }

    private void validEmail(CharSequence target) {//验证邮箱格式
        boolean trueOfFalse = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        if (!trueOfFalse){
            emailEditText.setError("Invalid email address");
            button.setEnabled(false);
        }else
            emailEditText.setError(null);
    }

    private void validatePassword(String password) {
        // 这里可以使用正则表达式等进行更复杂的密码验证逻辑
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$")) {
            passwordLayout.setError("Password must contain letter, number, and special character (@#$%^&+=), and be at least eight characters long");
            button.setEnabled(false);
        } else {
            passwordLayout.setError(null);
            checkEnableRegisterButton();
        }
    }

    private void validateSecondPassword(String secondPassword,String password){
        if (!secondPassword.equals(password)){
            secondpasswordLayout.setError("Two passwords are not same");
            button.setEnabled(false);
        }else {
            secondpasswordLayout.setError(null);
            checkEnableRegisterButton();
        }
    }

    private void checkEnableRegisterButton() {//发现输入的没毛病，按钮就可以点击
        if (usernameLayout.getError() == null && passwordLayout.getError() == null&&emailLayout.getError()==null) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
    }

    public void inHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }//进入主界面

    public void initUser(String username,String password,String email,BlogUser user){
        user.setDisplayName(username);
        user.setEmail(email);
        user.setPassword(password);
        LocalDate now = LocalDate.now();
        user.setSignupTime(now.toString());//因为json都是用字符串当作键的，这里用localdate的话
        user.setUserStatus(BUserStatusEnum.ACTIVE);
        user.setAboutMe(null);
        user.setBlogHistories(null);
        user.setProfilePicture(null);
        user.setLocation(null);
        user.setGithubLink(null);
        user.setBlogHistories(null);
        user.setLinkedinLink(null);
        user.setMyTechStack(null);
        user.setPostedBlogs(null);
    }
}