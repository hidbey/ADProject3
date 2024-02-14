package iss.workshop.adproject.Model;

import java.io.Serializable;

public class Comment implements Serializable {
    private int commentId;
    private String commentContent;
    private Blog commentBlog;
    private BlogUser commentBlogUser; // 格式化的时间字符串

    // 构造方法、getter 和 setter

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Blog getCommentBlog() {
        return commentBlog;
    }

    public void setCommentBlog(Blog commentBlog) {
        this.commentBlog = commentBlog;
    }

    public BlogUser getCommentBlogUser() {
        return commentBlogUser;
    }

    public void setCommentBlogUser(BlogUser commentBlogUser) {
        this.commentBlogUser = commentBlogUser;
    }

    public Comment(int commentId, String commentContent, Blog commentBlog, BlogUser commentBlogUser) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentBlog = commentBlog;
        this.commentBlogUser = commentBlogUser;
    }

    public Comment(){}
}
