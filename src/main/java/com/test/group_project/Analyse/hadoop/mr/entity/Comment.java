package com.test.group_project.Analyse.hadoop.mr.entity;

public class Comment {
    private int commentStar;
    private String commentContent;

    public int getCommentStar() {
        return commentStar;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentStar=" + commentStar +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }

    public void setCommentStar(int commentStar) {
        this.commentStar = commentStar;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Comment(int commentStar, String commentContent) {
        this.commentStar = commentStar;
        this.commentContent = commentContent;
    }

    public Comment() {
    }
}
