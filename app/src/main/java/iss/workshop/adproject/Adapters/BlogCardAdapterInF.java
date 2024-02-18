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
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.R;

public class BlogCardAdapterInF extends RecyclerView.Adapter<BlogCardAdapterInF.BlogCardViewHolder>{
    List<Blog> blogs;
    private Context context;

    public BlogCardAdapterInF(List<Blog> blogs, Context context) {
        this.blogs = blogs;
        this.context = context;
    }

    public void setBlogs(List<Blog>blogs){
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogCardAdapterInF.BlogCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_card, parent, false);
        return new BlogCardAdapterInF.BlogCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogCardAdapterInF.BlogCardViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(blogs.get(position).getBlogUser().getProfilePicture())
                .circleCrop()
                .into(holder.imageView);
        Glide.with(holder.itemView.getContext())
                .load(blogs.get(position).getImage())
                .into(holder.coverView);

        holder.blogTitle.setText(blogs.get(position).getContentTitle());
        holder.blogAuthor.setText(blogs.get(position).getBlogUser().getDisplayName());
        holder.blogLikes.setText(String.valueOf(blogs.get(position).getBlogLikeCount()));
        holder.blogComments.setText(String.valueOf(blogs.get(position).getBlogCommentCount()));
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    class BlogCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView,coverView;
        TextView blogTitle;
        TextView blogAuthor;
        TextView blogLikes;
        TextView blogComments;


        public BlogCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_picture);
            blogTitle = itemView.findViewById(R.id.item_title);
            blogAuthor = itemView.findViewById(R.id.item_username);
            blogLikes = itemView.findViewById(R.id.blogLikes);
            blogComments = itemView.findViewById(R.id.blogComments);
            coverView = itemView.findViewById(R.id.item_cover);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Blog blog = blogs.get(position);
                // 启动 BlogDetailActivity，并传递标题数据
                Intent intent = new Intent(context, BlogDetailActivity.class);
                intent.putExtra("blogId", blog.getBlogId());
                intent.putExtra("blogInList", blog); // TODO new new new
                intent.putExtra("position", position); // TODO new new new
                intent.putExtra("from","favorite");
                context.startActivity(intent);
            }
        }
    }

    public void updateBlogs(List<Blog> newBlogs) {
        this.blogs.clear();
        this.blogs.addAll(newBlogs);
        notifyDataSetChanged();
    }

}