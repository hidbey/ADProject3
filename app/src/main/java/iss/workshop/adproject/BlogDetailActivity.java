package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.Adapters.CommentAdapter;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.Comment;

public class BlogDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        Intent intent  = getIntent();
        Blog blog = (Blog)intent.getSerializableExtra("blog");
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView contentTextView = findViewById(R.id.contentTextView);

        titleTextView.setText(blog.getContentTitle());
        contentTextView.setText(blog.getContent());

        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        // TODO: 从服务器获取评论
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("User1", "This is comment 1", "11"));
        comments.add(new Comment("User2", "This is comment 2", "11"));
        comments.add(new Comment("User3", "This is comment 3", "11"));
        CommentAdapter adapter = new CommentAdapter(comments); // 假设 commentsList 是从服务器获取的评论列表
        rvComments.setAdapter(adapter);

        ImageButton btnLike = findViewById(R.id.btn_like);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());  // 切换选中状态
                // 可以在这里添加实际的点赞逻辑
            }
        });

        ImageButton btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());  // 切换选中状态
                // 可以在这里添加实际的点赞逻辑
            }
        });
    }
}