package iss.workshop.adproject.Model;

import java.util.List;

public class BlogGroup {
    private String groupName;
    private List<Blog> blogs;
    private boolean isExpanded = false;


    public BlogGroup() {
    }

    public BlogGroup(String groupName, List<Blog> blogs, boolean isExpanded) {
        this.groupName = groupName;
        this.blogs = blogs;
        this.isExpanded = isExpanded;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
