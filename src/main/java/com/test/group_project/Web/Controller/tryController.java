package com.test.group_project.Web.Controller;

import com.test.group_project.JDBC.Controller.Movie_allController;
import com.test.group_project.JDBC.domain.po.Details;
import com.test.group_project.JDBC.domain.po.Wordcloud;
import com.test.group_project.JDBC.domain.po.movie_all;
import com.test.group_project.JDBC.domain.po.shitilei;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class tryController {

    @GetMapping("/searchdata")
    @ResponseBody
    public shitilei tr(String search) throws JSONException, IOException {
        System.out.println(search);
        List<Wordcloud> wordclouds=WordCloudWrite(search);
        List<Details> detailses=DetailsWrite(search);
        return new shitilei(wordclouds,detailses);
    }

    public static List<Wordcloud> WordCloudWrite(String movie_name) throws JSONException {
        Movie_allController movie_allController = new Movie_allController();

        List<Wordcloud> wordclouds=new ArrayList<>();


        movie_all ma = movie_allController.select(movie_name);
        String hotkeys = ma.getMovie_hotkeys();
        JSONObject raw_hotkeys = new JSONObject(hotkeys);
        Iterator keys = raw_hotkeys.keys();
        while (keys.hasNext()){
            Object key = keys.next();
            Wordcloud wordcloud=new Wordcloud((String)key, (Integer) raw_hotkeys.get((String) key));
            wordclouds.add(wordcloud);
        }

        JSONObject jsonDetail = new JSONObject();
        jsonDetail.put("movie",ma.getMovie_name());
        jsonDetail.put("type",ma.getMovie_types());
        jsonDetail.put("value",ma.getMovie_rate());
        return wordclouds;
    }
    public static List<Details> DetailsWrite(String movie_name) {
        Movie_allController movie_allController = new Movie_allController();

        List<Details> detailses=new ArrayList<>();

        movie_all ma = movie_allController.select(movie_name);

        Details details = new Details(ma.getMovie_name(),ma.getMovie_types(),ma.getMovie_rate());
        detailses.add(details);
        return detailses;

    }
}
