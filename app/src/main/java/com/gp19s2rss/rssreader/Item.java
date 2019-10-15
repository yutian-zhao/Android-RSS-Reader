package com.gp19s2rss.rssreader;


import java.util.Date;

public class Item {
    String channel;
    String title;
    String link;
    String description;
    Date date;

    public void Item() {
    }

    public String getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}