package com.gp19s2rss.rssreader;


public class Item {
    String channel;
    String title;
    String link;
    String description;
    String date;

    public void Item (){};

//    public void Item (){}
//    public Item(String channel, String title, String link, String description, String date) {
//        this.channel = channel;
//        this.title = title;
//        this.link = link;
//        this.description = description;
//        this.date = date;
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

    public String getDate() {
        return date;
    }
}