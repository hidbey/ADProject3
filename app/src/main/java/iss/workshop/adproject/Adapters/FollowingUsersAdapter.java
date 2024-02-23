package iss.workshop.adproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import iss.workshop.adproject.Model.BlogUser;
import iss.workshop.adproject.R;
import iss.workshop.adproject.UserDetailActivity;

public class FollowingUsersAdapter extends RecyclerView.Adapter<FollowingUsersAdapter.ViewHolder> {

    private List<BlogUser> users;
    private Context context;

    public FollowingUsersAdapter(List<BlogUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    public void setUsers(List<BlogUser>users){
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BlogUser user = users.get(position);
        holder.userName.setText(user.getDisplayName()+" @ "+user.getLocation());
        holder.aboutMe.setText(user.getAboutMe());

        Glide.with(context)
                .load(user.getProfilePicture())
                .circleCrop()
                .into(holder.avatar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("userId", user.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView userName;
        TextView aboutMe;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.ivUserAvatar);
            userName = itemView.findViewById(R.id.tvUserNameLocation);
            aboutMe = itemView.findViewById(R.id.tvAboutMe);
        }
    }
}
