package iss.workshop.adproject.Model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class BlogUser implements Serializable {
    private int userId;
    private String displayName;
    private String email;
    private String password;
    private String signupTime;
    private String profilePicture;
    private String profileTagline;
    private String location;
    private String aboutMe;
    private String myTechStack;
    private String githubLink;
    private String linkedinLink;
    private BUserStatusEnum userStatus;
    private List<BlogUser> following;
    private List<BlogUser> followers;
    private List<BlogHistory> blogHistories;
    private List<Blog> postedBlogs;

    public BlogUser() {}
    public BlogUser( String displayName,
                     String email,
                     String password, String signupTime,
                     String profilePicture, String profileTagline, String location, String aboutMe, String myTechStack,
                     String githubLink, String linkedinLink, BUserStatusEnum userStatus, List<BlogUser> followings,
                     List<BlogUser> followers, List<BlogHistory> blogHistories, List<Blog> postedBlogs) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.signupTime = signupTime;
        this.profilePicture = profilePicture;
        this.profileTagline = profileTagline;
        this.location = location;
        this.aboutMe = aboutMe;
        this.myTechStack = myTechStack;
        this.githubLink = githubLink;
        this.linkedinLink = linkedinLink;
        this.userStatus = userStatus;
        this.following = followings;
        this.followers = followers;
        this.blogHistories = blogHistories;
        this.postedBlogs = postedBlogs;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSignupTime() {
        return signupTime;
    }
    public void setSignupTime(String signupTime) {
        this.signupTime = signupTime;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getProfileTagline() {
        return profileTagline;
    }
    public void setProfileTagline(String profileTagline) {
        this.profileTagline = profileTagline;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getAboutMe() {
        return aboutMe;
    }
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    public String getMyTechStack() {
        return myTechStack;
    }
    public void setMyTechStack(String myTechStack) {
        this.myTechStack = myTechStack;
    }
    public String getGithubLink() {
        return githubLink;
    }
    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }
    public String getLinkedinLink() {
        return linkedinLink;
    }
    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }
    public BUserStatusEnum getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(BUserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }
    public List<BlogUser> getFollowings() {
        return following;
    }
    public void setFollowing(List<BlogUser> followings) {
        this.following = followings;
    }
    public List<BlogUser> getFollower() {
        return followers;
    }
    public void setFollowers(List<BlogUser> followers) {
        this.followers = followers;
    }
    public List<BlogHistory> getBlogHistories() {
        return blogHistories;
    }
    public void setBlogHistories(List<BlogHistory> blogHistories) {
        this.blogHistories = blogHistories;
    }
    public List<Blog> getPostedBlogs() {
        return postedBlogs;
    }
    public void setPostedBlogs(List<Blog> postedBlogs) {
        this.postedBlogs = postedBlogs;
    }
    @Override
    public String toString() {
        return "BlogUser [userId=" + userId + ", displayName=" + displayName + ", email=" + email + ", password="
                + password + ", signupTime=" + signupTime + ", profilePicture=" + profilePicture + ", profileTagline="
                + profileTagline + ", location=" + location + ", aboutMe=" + aboutMe + ", myTechStack=" + myTechStack
                + ", githubLink=" + githubLink + ", linkedinLink=" + linkedinLink + ", userStatus=" + userStatus
                + ", followings=" + following + ", followers=" + followers + ", blogHistories=" + blogHistories
                + ", postedBlogs=" + postedBlogs + "]";
    }
}
