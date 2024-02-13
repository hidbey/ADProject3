package iss.workshop.adproject.Model;

import java.io.Serializable;
import java.util.List;

public class Blog implements Serializable {
    private int blogId;
    private String image;
    private int readingTime;
    private String contentTitle;
    private String subTitle;
    private String content;
    private String blogTime;
    private List<String> languageSelected;
    private List<String> techniqueSelected;
    private BlogStatusEnum blogStatus;
    private int blogCommentCount;
    private int blogLikeCount;

    private List<BlogHistory> blogHistories;

    private BlogUser blogUser;

    public Blog() {
    }

    public Blog(String image, int readingTime, String contentTitle, String subTitle, String content, String blogTime,
                List<String> languageSelected, List<String> techniqueSelected, BlogStatusEnum blogStatus,
                int blogCommentCount, int blogLikeCount, List<BlogHistory> blogHistories, BlogUser blogUser) {
        this.image = image;
        this.readingTime = readingTime;
        this.contentTitle = contentTitle;
        this.subTitle = subTitle;
        this.content = content;
        this.blogTime = blogTime;
        this.languageSelected = languageSelected;
        this.techniqueSelected = techniqueSelected;
        this.blogStatus = blogStatus;
        this.blogCommentCount = blogCommentCount;
        this.blogLikeCount = blogLikeCount;
        this.blogHistories = blogHistories;
        this.blogUser = blogUser;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(int readingTime) {
        this.readingTime = readingTime;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(String blogTime) {
        this.blogTime = blogTime;
    }

    public List<String> getLanguageSelected() {
        return languageSelected;
    }

    public void setLanguageSelected(List<String> languageSelected) {
        this.languageSelected = languageSelected;
    }

    public List<String> getTechniqueSelected() {
        return techniqueSelected;
    }

    public void setTechniqueSelected(List<String> techniqueSelected) {
        this.techniqueSelected = techniqueSelected;
    }

    public BlogStatusEnum getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(BlogStatusEnum blogStatus) {
        this.blogStatus = blogStatus;
    }

    public int getBlogCommentCount() {
        return blogCommentCount;
    }

    public void setBlogCommentCount(int blogCommentCount) {
        this.blogCommentCount = blogCommentCount;
    }

    public int getBlogLikeCount() {
        return blogLikeCount;
    }

    public void setBlogLikeCount(int blogLikeCount) {
        this.blogLikeCount = blogLikeCount;
    }

    public List<BlogHistory> getBlogHistories() {
        return blogHistories;
    }

    public void setBlogHistories(List<BlogHistory> blogHistories) {
        this.blogHistories = blogHistories;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    @Override
    public String toString() {
        return "Blog [blogId=" + blogId + ", image=" + image + ", readingTime=" + readingTime + ", contentTitle="
                + contentTitle + ", subTitle=" + subTitle + ", content=" + content + ", blogTime=" + blogTime
                + ", languageSelected=" + languageSelected + ", techniqueSelected=" + techniqueSelected
                + ", blogStatus=" + blogStatus + ", blogCommentCount=" + blogCommentCount + ", blogLikeCount="
                + blogLikeCount + "]";
    }


}
