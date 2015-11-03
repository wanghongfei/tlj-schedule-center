package com.taolijie.schedule.http.spark.model;


import java.util.List;

/**
 * Created by whf on 11/3/15.
 */
public class JsonList<T> {
    public static JsonList obj = new JsonList<>(null);

    private List<T> list;

    public JsonList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
