package com.test.group_project.Analyse.hadoop.mr.utils;
// 在 reducer 端进行数据
// 记得在 resource下添加本地运行的配置文件

import com.alibaba.fastjson.JSONObject;
import com.test.group_project.Analyse.hadoop.mr.entity.MovieDetailsBean;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.nativeio.NativeIO;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapred.lib.db.DBConfiguration;
import org.apache.hadoop.mapred.lib.db.DBInputFormat;
import org.apache.hadoop.mapred.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.io.IOException;
import java.util.*;

public class MovieReduceSideJoin {
    // 如果是 windows OS, 设置 环境变量
    // only for based windows run|debug MR
    static {
        // 判断当前执行环境是否为 windows,如果是,
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            // 设置远程操作 hdfs 的用户
            System.setProperty("HADOOP_USER_NAME", "vagrant");
            // 设置下载的winutils中的对应版本的 windows hadoop 可执行文件的位置
            // 位置: 可执行文件所在的 bin 目录的所在的位置
            System.setProperty("hadoop.home.dir", "F:/hadoop/hadoop3");
            // 设置没有空格和中文的路径作为 mr 的临时目录
            System.setProperty("hadoop.tmp.dir", "D:/output");
        }
    }


    // 每次改名！！！
    private static Configuration getMyConfiguration(){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://master:9000");
        conf.set("mapreduce.app-submission.cross-platform", "true");//跨平台提交，在windows下如果没有这句代码会报错 "/bin/bash: line 0: fg: no job control"，去网上很多都说是linux和windows环境不同导致的一般都是修改YarnRunner.java，但是其实添加了这行代码就可以了。
        conf.set("mapreduce.framework.name", "yarn");//集群的方式运行，非本地运行。
        String resourceManager="master";
        conf.set("yarn.resourcemanager.address", resourceManager + ":8032"); // 指定resourcemanager
        conf.set("yarn.resourcemanager.scheduler.address", resourceManager + ":8030");// 指定资源分配器
        conf.set("mapreduce.jobhistory.address", resourceManager + ":10020");
        // 提前规划好 jar 打包之后的位置，打包后，将 jar 复制到对应的位置，也可以使用 XxxClass.class.getClassLoader().getResource("//").toString();
        // 的方式，获取对应的位置，并拼接成对应的位置的字符串。
        // 结合 job.setJarByClass(UsersAndRatings.class)， 可以在 Windows 下和 Linux 下均可以提交运行。下述两个 key 均可以的。
        // conf.set("mapreduce.job.jar", "C:/Users/txsliwei/Desktop/Workspace/IdeaProjects/knnbymr/target/myknn_01_usersandratings.jar");
//        conf.set("mapred.jar", "D:\\20210712-南开大学泰达校区-大数据\\Workspace\\IdeaProjects\\nkhadoop\\target\\mymr.jar");
        conf.set("mapred.jar","d:/vagrant_need/mymrms.jar");
        conf.set("mapreduce.framework.name","local");
        conf.set("fs.defaultFS","file:///");
        return conf;
        /*
        //本地调试模式
         conf.set("mapreduce.framework.name","local");
         conf.set("fs.defaultFS","file:///");
        // 本地提交模式
        // conf.set("mapreduce.framework.name","local");
        // conf.set("fs.defaultFS","hdfs://master:9000");
        */
    }
    //确认继承的 输入 和 输出   //输入数据是 movieId,输出是 Bean
    public static class ReduceSideJoinMapper extends Mapper< LongWritable,Text , IntWritable, MovieDetailsBean>{

        protected void map(LongWritable key, Text value , Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("/");
            MovieDetailsBean movieDetailsBean = new MovieDetailsBean();
            int movieId = Integer.parseInt(fields[0]);
            movieDetailsBean.setMovieId(movieId);
            movieDetailsBean.setMovieName(fields[1]);
            movieDetailsBean.setMovieType(fields[2]);
            movieDetailsBean.setCommentId(Integer.parseInt(fields[3]));
            if(fields.length>=5)
                movieDetailsBean.setStar(Integer.parseInt(fields[4]));
            else
                movieDetailsBean.setStar(5000);
            if(fields.length>=6)
                movieDetailsBean.setContent(fields[5]);
            else
                movieDetailsBean.setContent("人和狗");
            movieDetailsBean.setMovieRate(0);
            movieDetailsBean.setHotKeys("");

            context.write(new IntWritable(movieId),movieDetailsBean);
        }

    }

    //Reducer 的输入是 movieId,输出是json文件
    public static class ReduceSideJoinReducer extends Reducer<IntWritable,MovieDetailsBean,NullWritable,Text>{
        @Override
        protected void reduce(IntWritable key, Iterable<MovieDetailsBean> values, Context context) throws IOException, InterruptedException {
            double movieRate = 0;
            double totalRate = 0;
            int num = 0;
            int movieId = 0;
            String movieName =  "";
            String movieType = "";
            List<String> content = new ArrayList<>();
            for(MovieDetailsBean bean:values){
                if(bean.getStar()>=0&&bean.getStar()<=9) {
                    totalRate += bean.getStar();
                    num++;
                }
                content.add(bean.getContent());
                movieId = bean.getMovieId();
                movieName = bean.getMovieName();
                movieType = bean.getMovieType();
            }
            movieRate = totalRate/num;
            String CommentsTotal = "";
            //将同一个 key:movieId 下的所有 短评合并在一起，目的是为了求热词
            for(int i = 0;i<content.size();i++)
                CommentsTotal = CommentsTotal+content.get(i);
            //进行分词，并移除停用词  (the problem is in the CommentsTotal)
            List<Word> words = WordSegmenter.seg(CommentsTotal);
            //对分词结果进行频率统计
            List<String> wordStr = new ArrayList<>();
            for(int i = 0;i<words.size();i++){
                wordStr.add(words.get(i).toString());
            }
            Map<String,Long> map = new TreeMap<>();
            for(int i = 0;i<wordStr.size();i++) {
                if(map.get(wordStr.get(i))!=null)
                    map.put(wordStr.get(i),map.get(wordStr.get(i))+1 );
                else
                    map.put(wordStr.get(i),1l);
            }
            //对map进行排序
            Map<String,Long> sortMap = new LinkedHashMap<>();
            map.entrySet().stream()
                    .sorted((o1,o2)->o2.getValue().compareTo(o1.getValue()))
                    .forEach(entry->sortMap.put(entry.getKey(),entry.getValue()));
            //并输出为json格式
            String hotkeys = JSONObject.toJSONString(sortMap);

            //将数据传到类对象movieDetailBean里
            MovieDetailsBean movieDetailsBean = new MovieDetailsBean();
            movieDetailsBean.setMovieId(movieId);
            movieDetailsBean.setMovieName(movieName);
            movieDetailsBean.setMovieType(movieType);
            movieDetailsBean.setMovieRate(movieRate);
            movieDetailsBean.setHotKeys(hotkeys);
            context.write(null,new Text(movieDetailsBean.toString()));
        }
    }


  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 0. 初始化 MR job
        Configuration configuration = getMyConfiguration();

        Job job = Job.getInstance(configuration);
        job.setJobName("GetMovieRateAndHotKeys");
        // 指明该 mr 使用那个类
        job.setJarByClass(MovieReduceSideJoin.class);

      FileInputFormat.setInputPaths(job, new Path(args[0]));
      job.setInputFormatClass(TextInputFormat.class);
      // 2. 指定 Mapper 类 & 输出的key&value的类型
        job.setMapperClass(MovieReduceSideJoin.ReduceSideJoinMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(MovieDetailsBean.class);


        // 3. 指定 Reducer 类 & 最终输出key&value的类型
        job.setNumReduceTasks(2);   // 默认 1, 设置为0==不执行 reducer
        job.setReducerClass(MovieReduceSideJoin.ReduceSideJoinReducer.class);
//        job.setOutputKeyClass(Text.class);    // ==> 2
        job.setOutputKeyClass(NullWritable.class);
//        job.setOutputValueClass(JoinBean.class);  // ==> 1
        job.setOutputValueClass(TextOutputFormat.class);



      FileOutputFormat.setOutputPath(job, new Path(args[1]));
      job.setOutputFormatClass(TextOutputFormat.class);

          // 5. 提交 mrjob
        // job.submit();
        boolean result = job.waitForCompletion(true);
        boolean isSuccess = job.isSuccessful();
        System.exit(isSuccess ? 0 : 1);
    }

}

