package iss.workshop.adproject.Adapters;

import android.content.Context;
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

import java.util.Arrays;
import java.util.List;

import iss.workshop.adproject.Model.BlogUser;
import iss.workshop.adproject.R;

public class ProfileSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CARD1 = 0;
    private static final int CARD2 = 1;
    private static final int CARD3 = 2;

    private final BlogUser user;

    public ProfileSettingAdapter(BlogUser user) {
        this.user = user;

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
            userNameEditText = itemView.findViewById(R.id.editTextDisplayName);
            emailEditText = itemView.findViewById(R.id.editTextEmail);
            locationEditText = itemView.findViewById(R.id.editTextLocation);
        }

        public void bind(BlogUser user){
            userNameEditText.setText(user.getDisplayName());
            emailEditText.setText(user.getEmail());
            locationEditText.setText(user.getLocation());
        }
    }

    class Card2ViewHolder extends RecyclerView.ViewHolder{
        EditText profileTagLineEditText;
        ImageView btnAddTech;
        EditText editText;
        FlexboxLayout flexboxLayout;

        public Card2ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTagLineEditText = itemView.findViewById(R.id.editTextProfileTagLine);
            editText = itemView.findViewById(R.id.etTechStack);
            flexboxLayout =itemView.findViewById(R.id.flexboxLayout);
            btnAddTech = itemView.findViewById(R.id.btnAddTech);

            btnAddTech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tech = editText.getText().toString().trim();
                    if (!tech.isEmpty()) {
                        // 创建 TextView 作为技术栈标签
                        TextView techView = new TextView(view.getContext());
                        techView.setText(tech);
                        techView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tech_stack_label_border));
                        //techView.setBackgroundResource(R.drawable.tech_stack_label_background); // 背景样式
                        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                FlexboxLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(12, 12, 12, 12); // 设置外边距
                        techView.setLayoutParams(layoutParams);
                        techView.setPadding(20, 10, 20, 10);
                        techView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                        // 将标签添加到 FlexboxLayout
                        flexboxLayout.addView(techView);
                        editText.setText("");
                    }
                }
            });
        }

        public void bind(BlogUser user){
            profileTagLineEditText.setText(user.getProfileTagline());

            String myTechStack = user.getMyTechStack();
            List<String>techs = Arrays.asList(myTechStack.split(","));
            for (String tech:techs) {
                TextView techView = new TextView(itemView.getContext());//要提供有效的上下文来创建视图
                techView.setText(tech.trim());
                techView.setText(tech.trim());
                techView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.tech_stack_label_border));
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(12, 12, 12, 12); // 设置外边距
                techView.setLayoutParams(layoutParams);
                techView.setPadding(20, 10, 20, 10);

                flexboxLayout.addView(techView);
            }
        }
    }

    class Card3ViewHolder extends RecyclerView.ViewHolder{

        EditText githubEditText,linkedinEditText,aboutmeEditText;

        public Card3ViewHolder(@NonNull View itemView) {
            super(itemView);

            githubEditText = itemView.findViewById(R.id.editTextGithub);
            linkedinEditText  = itemView.findViewById(R.id.editTextLinkedin);
            aboutmeEditText = itemView.findViewById(R.id.editTextAboutme);
        }
    }

}
