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

import iss.workshop.adproject.BlogDetailActivity;
import iss.workshop.adproject.Model.Comment;
import iss.workshop.adproject.R;
import iss.workshop.adproject.UserDetailActivity;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Context context;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvUsername.setText(comment.getCommentBlogUser().getDisplayName());
        holder.tvCommentText.setText(comment.getCommentContent());
//       holder.ivUserAvatar.setText("yhh");
        Glide.with(context)
                .load(comment.getCommentBlogUser().getProfilePicture())
                .circleCrop()
                .into(holder.ivUserAvatar);
        View.OnClickListener authorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("userId", comment.getCommentBlogUser().getUserId());
                context.startActivity(intent);
            }
        };
        holder.tvUsername.setOnClickListener(authorClickListener);
        holder.ivUserAvatar.setOnClickListener(authorClickListener);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvCommentText;
        ImageView ivUserAvatar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
        }
    }
}