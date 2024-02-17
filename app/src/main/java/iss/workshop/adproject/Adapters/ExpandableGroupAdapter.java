package iss.workshop.adproject.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.BlogDetailActivity;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogGroup;
import iss.workshop.adproject.R;

public class ExpandableGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BlogGroup> blogGroups;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ExpandableGroupAdapter(Context context, List<BlogGroup> blogGroups) {//
        this.context = context;
        this.blogGroups = blogGroups;
    }

    public void setBlogGroups(List<BlogGroup> blogGroups) {
        this.blogGroups = blogGroups;
        List<Blog>blogs = blogGroups.get(0).getBlogs();
//        if (blogs.size()!=0){
//            Blog blog = blogs.get(blogs.size()-1);
//            addBlogToFirstGroup(blog);
//        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (positionIsHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean positionIsHeader(int position) {
        int cumulatedSize = 0;//用来指下一个头部在的位置，如果position等于这个值，说明当前位置上的view是头部
        for (BlogGroup group : blogGroups) {
            if (position == cumulatedSize) {
                return true; // Position matches a group header
            }
            cumulatedSize++; // For the header
            if (group.isExpanded()&&group.getBlogs()!=null) {
                cumulatedSize += group.getBlogs().size(); // Add items if group is expanded
            }
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            BlogGroup group = getGroupByPosition(position);
            ((HeaderViewHolder) holder).bind(group);
        } else if (holder instanceof TitleViewHolder) {
            Blog blog = getBlogByPosition(position);
            ((TitleViewHolder) holder).bind(blog);
        }
    }

    public void addBlogToFirstGroup(Blog newBlog) {

            BlogGroup firstGroup = blogGroups.get(0);
            firstGroup.getBlogs().add(firstGroup.getBlogs().size(), newBlog); // 在列表开始处添加
            //通知RecyclerView在第一个header直接底下插入了新条目
            notifyItemInserted(firstGroup.getBlogs().size()+1);

    }

    private BlogGroup getGroupByPosition(int position) {
        int cumulatedSize = 0;
        for (BlogGroup group : blogGroups) {
            if (position == cumulatedSize) {
                return group;
            }
            cumulatedSize++; // Skip header
            if (group.isExpanded()) {
                cumulatedSize += group.getBlogs().size();
            }
        }
        return null;
    }

    private Blog getBlogByPosition(int position) {
        int cumulatedSize = 0;
        for (BlogGroup group : blogGroups) {
            cumulatedSize++; // Skip header
            if (group.isExpanded()) {
                if (position < cumulatedSize + group.getBlogs().size()) {
                    return group.getBlogs().get(position - cumulatedSize);
                }
                cumulatedSize += group.getBlogs().size();
            }
        }
        return null; // Should never happen
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (BlogGroup group : blogGroups) {
            itemCount++; // For the header
            if (group.isExpanded()) {
                if (group.getBlogs()!=null){
                    itemCount += group.getBlogs().size();
                }
            }
        }
        return itemCount;
    }

    class TitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView blogTitle;
        //TextView blogAuthor;
        TextView blogLikes;
        TextView blogViews;


        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blogTitle);
           // blogAuthor = itemView.findViewById(R.id.blogAuthor);
            blogLikes = itemView.findViewById(R.id.blogLikes);
            blogViews = itemView.findViewById(R.id.blogViews);

            itemView.setOnClickListener(this);
        }

        public void bind(Blog blog){
            this.blogTitle.setText(blog.getContentTitle());
            //this.blogAuthor.setText(blog.getBlogUser().getDisplayName());
            this.blogLikes.setText(String.valueOf(blog.getBlogLikeCount()));
            this.blogViews.setText(String.valueOf(blog.getBlogCommentCount()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Blog blog = getBlogByPosition(position); //调用getBlogByPosition来获取到当前位置的blog
                if (blog != null) {
                    Intent intent = new Intent(context, BlogDetailActivity.class);
                    intent.putExtra("blogId", blog.getBlogId());
                    intent.putExtra("blogInList", blog); // TODO new new new
                    intent.putExtra("position", position); // TODO new new new
                    intent.putExtra("from","posted");
                    context.startActivity(intent);
                }
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;
        ImageView expandIndicator;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.tvGroupHeader);
            expandIndicator = itemView.findViewById(R.id.ivExpandIndicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BlogGroup group = getGroupByPosition(position);
                    if (group != null) {
                        group.setExpanded(!group.isExpanded());
                        if (group.isExpanded()) {
                            int startPos = getGroupStartPos(group) + 1; //获取组开始的位置，加1跳过头部
                            for (int i = 0; i < group.getBlogs().size(); i++) {
                                notifyItemInserted(startPos + i);//进行i次循环去提醒适配器插入新的数据达到更新ui的效果
                            }
                        }else {
                            int startPos = getGroupStartPos(group) + 1; // 同样获取组开始的位置，加1跳过头部
                            for (int i = group.getBlogs().size() - 1; i >= 0; i--) {
                                notifyItemRemoved(startPos + i);//和上面一样
                            }
                        }
                        rotateIndicator(group.isExpanded()); // 根据组的展开状态旋转指示器
                    }
                }
            });
        }

        private int getGroupStartPos(BlogGroup group){
            int pos = 0;//从头开始遍历每一个bloggroup，如果到达一个group名字和你惦记的一样，说明到达，
            for (BlogGroup blogGroup:blogGroups) {
                if (blogGroup.getGroupName().equals(group.getGroupName())){
                    return pos;
                }
                pos++;
                if (blogGroup.isExpanded()){
                    pos += group.getBlogs() != null ? group.getBlogs().size() : 0;
                }
            }
            return -1;
        }

        private void rotateIndicator(boolean isExpanded) {
            float startDegree = isExpanded ? 0 : 90;
            float endDegree = isExpanded ? 90 : 0;
            ObjectAnimator animator = ObjectAnimator.ofFloat(expandIndicator, "rotation", startDegree, endDegree);
            animator.setDuration(300); // 动画持续时间
            animator.start();
        }

        public void bind(BlogGroup group) {
            headerTitle.setText(group.getGroupName());
            expandIndicator.setRotation(group.isExpanded() ? 90 : 0);
        }
    }
}
