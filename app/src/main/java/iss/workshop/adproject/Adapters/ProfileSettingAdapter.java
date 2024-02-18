package iss.workshop.adproject.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import iss.workshop.adproject.DataService.UserDataService;
import iss.workshop.adproject.Model.BlogUser;
import iss.workshop.adproject.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CARD1 = 0;
    private static final int CARD2 = 1;
    private static final int CARD3 = 2;

    private final BlogUser user;
    private Context context;

    public ProfileSettingAdapter(BlogUser user,Context context) {
        this.user = user;
        this.context = context;
    }

    public void saveUpdatedUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserDataService uDService = retrofit.create(UserDataService.class);
        Call<ResponseBody> call =uDService.updateUser(user.getUserId(),user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userName",null);
                }else{
                    Log.d("Retrofit", "Response Body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                return;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==CARD1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_email_location, parent, false);
            return new ProfileSettingAdapter.Card1ViewHolder(view);
        } else if (viewType==CARD2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profiletagline_mytechstack, parent, false);
            return new ProfileSettingAdapter.Card2ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.links_aboutme, parent, false);
            return new ProfileSettingAdapter.Card3ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Card1ViewHolder) {
            Card1ViewHolder viewHolder = (Card1ViewHolder) holder;
            viewHolder.bind(user);
        } else if (holder instanceof Card2ViewHolder) {
            Card2ViewHolder viewHolder = (Card2ViewHolder) holder;
            viewHolder.bind(user);
        } else if (holder instanceof Card3ViewHolder) {
            Card3ViewHolder viewHolder = (Card3ViewHolder) holder;
            viewHolder.githubEditText.setText(user.getGithubLink());
            viewHolder.linkedinEditText.setText(user.getLinkedinLink());
            viewHolder.aboutmeEditText.setText(user.getAboutMe());
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        // 根据位置返回视图类型
        switch (position) {
            case 0:
                return CARD1;
            case 1:
                return CARD2;
            case 2:
                return CARD3;
            default:
                return -1; // 可以用来指示一个无效的视图类型
        }
    }



    class Card1ViewHolder extends RecyclerView.ViewHolder{
        EditText userNameEditText,emailEditText,locationEditText;

        public Card1ViewHolder(@NonNull View itemView) {
            super(itemView);
            init();
        }

        public void bind(BlogUser user){
            userNameEditText.setText(user.getDisplayName());
            emailEditText.setText(user.getEmail());
            locationEditText.setText(user.getLocation());
        }

        public void init(){
            userNameEditText = itemView.findViewById(R.id.editTextDisplayName);
            emailEditText = itemView.findViewById(R.id.editTextEmail);
            locationEditText = itemView.findViewById(R.id.editTextLocation);
            userNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // 更新user对象的GitHub链接
                    user.setDisplayName(editable.toString());
                }
            });

            emailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    user.setEmail(editable.toString());
                }
            });

            locationEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    user.setLocation(editable.toString());
                }
            });
        }
    }

    class Card2ViewHolder extends RecyclerView.ViewHolder{
        EditText profileTagLineEditText;
        ImageView btnAddTech;
        EditText editText;
        FlexboxLayout flexboxLayout;
        String myTechStack=user.getMyTechStack();
        List<String> techs = Arrays.stream(myTechStack.split(","))
                .map(String::trim) //去除每个元素两侧的空格
                .collect(Collectors.toList());
        public Card2ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTagLineEditText = itemView.findViewById(R.id.editTextProfileTagLine);
            editText = itemView.findViewById(R.id.etTechStack);
            flexboxLayout =itemView.findViewById(R.id.flexboxLayout);
            btnAddTech = itemView.findViewById(R.id.btnAddTech);

            btnAddTech.setOnClickListener(view -> {
                String tech = editText.getText().toString().trim();
                if (!tech.isEmpty()) {
                    addTechStack(view.getContext(), tech); // 抽取添加技术栈的逻辑到方法中
                    editText.setText("");
                }
            });

            profileTagLineEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    user.setProfileTagline(editable.toString());
                }
            });
        }

        public void addTechStack(Context context,String tech){
            TextView techView = new TextView(context);
            techView.setText(tech);
            setupTechView(techView); // 设置 TextView 的外观和布局参数

            // 设置长按删除
            techView.setOnLongClickListener(v -> {
                flexboxLayout.removeView(techView); // 移除视图
                techs.remove(tech); // 从 techs 列表中移除对应的技术栈字符串

                return true;
            });

            // 添加到 FlexboxLayout 和 techs 列表
            flexboxLayout.addView(techView);
            techs.add(tech); // 确保这里添加的字符串格式与移除时的一致
            updateMyTechStack(); // 更新 user 对象的 myTechStack 字段
        }

        private void setupTechView(TextView techView) {
            techView.setBackground(ContextCompat.getDrawable(context, R.drawable.tech_stack_label_border));
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(12, 12, 12, 12);
            techView.setLayoutParams(layoutParams);
            techView.setPadding(20, 10, 20, 10);
        }

        private void updateMyTechStack() {
            // 更新 user 对象的 myTechStack 字段
            String techStacks = String.join(", ", techs);
            user.setMyTechStack(techStacks);


        }

        public void bind(BlogUser user){
            profileTagLineEditText.setText(user.getProfileTagline());
            for (String tech:techs) {
                TextView techView = new TextView(itemView.getContext());//要提供有效的上下文来创建视图
                techView.setText(tech.trim());
                techView.setText(tech.trim());
                techView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.tech_stack_label_border));
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(12, 12, 12, 12); //设置外边距
                techView.setLayoutParams(layoutParams);
                techView.setPadding(20, 10, 20, 10);

                techView.setOnLongClickListener(v -> {
                    flexboxLayout.removeView(techView);//长按时移除视图
                    techs.remove(techView.getText().toString());
                    updateMyTechStack(); // 更新 user 对象的 myTechStack 字段
                    return true; // 返回 true 表示事件已被处理
                });
                flexboxLayout.addView(techView);
            }
        }
    }

    class Card3ViewHolder extends RecyclerView.ViewHolder{
        EditText githubEditText,linkedinEditText,aboutmeEditText;
        public Card3ViewHolder(@NonNull View itemView) {
            super(itemView);
            init();
        }

        public void init(){
            githubEditText = itemView.findViewById(R.id.editTextGithub);
            linkedinEditText  = itemView.findViewById(R.id.editTextLinkedin);
            aboutmeEditText = itemView.findViewById(R.id.editTextAboutme);

            githubEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    user.setGithubLink(editable.toString());
                }
            });

            linkedinEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    user.setLinkedinLink(editable.toString());
                }
            });

            aboutmeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    user.setAboutMe(editable.toString());
                }
            });
        }
    }
}
