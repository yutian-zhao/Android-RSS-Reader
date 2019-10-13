package com.gp19s2rss.rssreader;

import java.util.Date;

public class Item {
    String channel;
    String title;
    String link;
    String description;
    Date date;
//    int id;
    // channel, title, time (extracted image)
    public void Item (){}
//    public Item(String channel, String title, String link, String description) {
//        this.channel = channel;
//        this.title = title;
//        this.link = link;
//        this.description = description;
////        this.id = id;
//    }

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

//    public int getId() {
//        return id;
//    }
}