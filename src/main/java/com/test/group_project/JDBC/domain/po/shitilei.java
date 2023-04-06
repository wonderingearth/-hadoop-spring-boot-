package com.test.group_project.JDBC.domain.po;
import java.util.List;

public class shitilei {
    List<Wordcloud> list1;
    List<Details> list2;

    @Override
    public String toString() {
        return "shitilei{" +
                "list1=" + list1 +
                ", list2=" + list2 +
                '}';
    }

    public List<Wordcloud> getList1() {
        return list1;
    }

    public void setList1(List<Wordcloud> list1) {
        this.list1 = list1;
    }

    public List<Details> getList2() {
        return list2;
    }

    public void setList2(List<Details> list2) {
        this.list2 = list2;
    }

    public shitilei(List<Wordcloud> list1, List<Details> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }
}
