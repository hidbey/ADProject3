package iss.workshop.adproject.Model;

public class Comment {
    private String username;
    private String commentText;
    private String timestamp; // 格式化的时间字符串

    // 构造方法、getter 和 setter
    public Comment(String username, String commentText, String timestamp) {
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
