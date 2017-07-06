package com.harbor.ormlite.hello;

import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Harbor on 2017/7/6.
 */

public class SimpleData{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(index = true)
    String string;
    @DatabaseField
    long millis;
    @DatabaseField
    Date date;
    boolean even;

    public SimpleData(long millis){
        this.date = new Date(millis);
        this.string = (millis % 1000) + "ms";
        this.millis = millis;
        this.even = ((millis % 2) == 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append(", ").append("str=").append(string);
        sb.append(", ").append("ms=").append(millis);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd HH:mm:ss", Locale.US);
        sb.append(", ").append("date=").append(dateFormatter.format(date));
        sb.append(", ").append("even=").append(even);
        return sb.toString();
    }
}
