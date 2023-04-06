package com.test.group_project.JDBC.domain.po;
//用于转化为json的类
public class SpiderOutput {
    String movieType;
    String movieName;
    int commentStar;
    String commentContent;

    public SpiderOutput() {
    }

    public SpiderOutput(String movieType, String movieName, int commentStar, String commentContent) {
        this.movieType = movieType;
        this.movieName = movieName;
        this.commentStar = commentStar;
        this.commentContent = commentContent;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getCommentStar() {
        return commentStar;
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

    @Override
    public String toString() {
        return "JsonObject{" +
                "movieType='" + movieType + '\'' +
                ", movieName='" + movieName + '\'' +
                ", commentStar='" + commentStar + '\'' +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }
}
