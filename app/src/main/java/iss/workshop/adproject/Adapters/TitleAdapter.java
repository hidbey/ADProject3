package iss.workshop.adproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iss.workshop.adproject.BlogDetailActivity;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.R;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder> {

    private List<Blog> titles;
    private Context context;

    public TitleAdapter(List<Blog> titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        holder.blogTitle.setText(titles.get(position).getContentTitle());
        //holder.blogAuthor.setText(titles.get(position).getBlogUser().getDisplayName());
        holder.blogLikes.setText(String.valueOf(titles.get(position).getBlogLikeCount()));
        holder.blogViews.setText(String.valueOf(titles.get(position).getBlogCommentCount()));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView blogTitle;
        //TextView blogAuthor;
        TextView blogLikes;
        TextView blogViews;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blogTitle);
            //blogAuthor = itemView.findViewById(R.id.blogAuthor);
            blogLikes = itemView.findViewById(R.id.blogLikes);
            blogViews = itemView.findViewById(R.id.blogViews);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Blog blog = titles.get(position);
                // 启动 BlogDetailActivity，并传递标题数据
                Intent intent = new Intent(context, BlogDetailActivity.class);
                intent.putExtra("blogId", blog.getBlogId());
                intent.putExtra("blogInList", blog); // TODO new new new
                intent.putExtra("position", position); // TODO new new new
                intent.putExtra("from","user");
                context.startActivity(intent);
            }
        }
    }
}
