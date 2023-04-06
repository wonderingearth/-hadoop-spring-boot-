package com.test.group_project.Spider;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.test.group_project.JDBC.Controller.*;
import com.test.group_project.JDBC.domain.po.*;
public class file_to_db {
    static void process() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader("C:\\Users\\13608\\IdeaProjects\\group_project\\src\\group_project\\src\\main\\java\\com\\test\\group_project\\Spider\\MovieResults.json");
        Object obj = parser.parse(reader);
        JSONArray movieList = (JSONArray) obj;
        movieList.forEach(mov -> parseObject((JSONObject) mov));
    }
    private static void parseObject(JSONObject movie){
        MovieController movieController = new MovieController();
        TypeController typeController = new TypeController();
        PeopleController peopleController = new PeopleController();
        Movie_peopleController movie_peopleController = new Movie_peopleController();

        movie mov = new movie(null,(String)movie.get("name"));
        movieController.setMov(mov);
        int movie_id = movieController.insert();
        if (movie_id == -1){
            return;
        }
        String[] types = ((String) movie.get("type")).split("/");
        for (int i =0;i<types.length;i++){
            type tp = new type(movie_id,types[i]);
            typeController.setTp(tp);
            typeController.insert();
        }

        String[] directors = ((String) movie.get("director")).split("/");
        for (int i =0;i<directors.length;i++){
            people peo = new people(null,directors[i],"director");
            peopleController.setPeo(peo);
            int people_id = peopleController.insert();

            movie_people mp = new movie_people(movie_id,people_id);
            movie_peopleController.setMp(mp);
            movie_peopleController.insert();
        }
        String[] actors = ((String) movie.get("actor")).split("/");
        for (int i =0;i<actors.length;i++) {
            people peo = new people(null, actors[i], "actor");
            peopleController.setPeo(peo);
            int people_id = peopleController.insert();

            movie_people mp = new movie_people(movie_id,people_id);
            movie_peopleController.setMp(mp);
            movie_peopleController.insert();
        }

        JSONArray commentList = (JSONArray) movie.get("comments");
        commentList.forEach(com -> parseCommentArray((JSONObject) com, movie_id));
        System.out.println("全部插入成功！");
    }
    private static void parseCommentArray(JSONObject comment,int movie_id){
            Long comment_star;
            String comment_content;
            String String_star = (String)(comment.get("star"));
            if (String_star.equals("m"))comment_star = 5000L;
            else comment_star = Long.parseLong(String_star);
            comment_content = (String) (comment.get("content"));
            CommentController commentController = new CommentController();
            comment com = new comment(movie_id,null,comment_star,comment_content);

            commentController.setCom(com);
            commentController.insert();
    }
}


