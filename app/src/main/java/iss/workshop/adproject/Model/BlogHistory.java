package iss.workshop.adproject.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class BlogHistory  implements Serializable {

    private int blogHistoryId;

    private Blog blog;

    private BlogUser blogUser;
    private String readDate;

    public BlogHistory() {}

    public BlogHistory(Blog blog, BlogUser blogUser, String readDate) {
        this.blog = blog;
        this.blogUser = blogUser;
        this.readDate = readDate;
    }

    public int getBlogHistoryId() {
        return blogHistoryId;
    }

    public void setBlogHistoryId(int blogHistoryId) {
        this.blogHistoryId = blogHistoryId;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    @Override
    public String toString() {
        return "BlogHistory [blogHistoryId=" + blogHistoryId + ", readDate=" + readDate + "]";
    }
}
