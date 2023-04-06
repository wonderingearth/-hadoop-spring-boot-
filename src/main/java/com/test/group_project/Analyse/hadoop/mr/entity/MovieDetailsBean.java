package com.test.group_project.Analyse.hadoop.mr.entity;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//这个类用于存储 hadoop mapreduce 分析出来的数据  数据主要内容是某一步具体的电影的  movie_id movie_anme movie_type movie_star 评分 以及 短评热词（应该是List<String>类型）
// 实现 hdfs文件读写 和数据库读写接口    Writable 实现对 mapper的序列化输入输出   DBWritable 实现对数据库的交互
public class MovieDetailsBean implements Writable, DBWritable {
    // 实体类里的属性
    int movieId = 0;
    String movieName = "";
    String movieType = "";
    int commentId = 0;
    String content = "";
    int star = 0;

    double movieRate = 0;
    String hotKeys = "";

    public MovieDetailsBean(int movieId, String movieName, String movieType, int commentId, String content, int star, double movieRate, String hotKeys) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieType = movieType;
        this.commentId = commentId;
        this.content = content;
        this.star = star;
        this.movieRate = movieRate;
        this.hotKeys = hotKeys;
    }

    @Override
    public String toString() {
        return
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                ", movieType='" + movieType + '\'' +
                ", movieRate=" + movieRate +
                ", hotKeys='" + hotKeys + '\'';
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public double getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(double movieRate) {
        this.movieRate = movieRate;
    }

    public String getHotKeys() {
        return hotKeys;
    }

    public void setHotKeys(String hotKeys) {
        this.hotKeys = hotKeys;
    }



    public MovieDetailsBean() {
    }
//this.movieId = movieId;
//        this.movieName = movieName;
//        this.movieType = movieType;
//        this.commentId = commentId;
//        this.content = content;
//        this.star = star;
//        this.movieRate = movieRate;
//        this.hotKeys = hotKeys;
    //实现两个读写接口
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(movieId);
        dataOutput.writeUTF(movieName);
        dataOutput.writeUTF(movieType);
        dataOutput.writeInt(commentId);
        dataOutput.writeInt(star);
        dataOutput.writeUTF(content);

        dataOutput.writeDouble(movieRate);
        dataOutput.writeUTF(hotKeys);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.movieId = dataInput.readInt();
        this.movieName = dataInput.readUTF();
        this.movieType = dataInput.readUTF();
        this.commentId = dataInput.readInt();
        this.star = dataInput.readInt();
        this.content = dataInput.readUTF();
        this.movieRate = dataInput.readDouble();
        this.hotKeys = dataInput.readUTF();
    }

    @Override
    public void write(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1,movieId);
        preparedStatement.setString(2,movieName);
        preparedStatement.setString(3,movieType);
        preparedStatement.setDouble(4,movieRate);
        preparedStatement.setString(5,hotKeys);

    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        resultSet.getInt(1);
        resultSet.getString(2);
        resultSet.getString(3);
        resultSet.getString(4);
        resultSet.getInt(5);
        resultSet.getString(6);
    }
}
