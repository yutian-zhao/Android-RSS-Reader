package com.gp19s2rss.rssreader;


import java.util.Date;

/**
 * <h1>Storage feeds information </h1>
 * This class-Item contains main information of RSS feeds,
 * including of channel, title, link,description and date;
 *
 * @version 1.0
 * @since 2019-10-08th
 */
public class Item {
    String channel;
    String title;
    String link;
    String description;
    Date date;

    public void Item() {
    }

    /**
     * this method will get channel from item
     * @return channel of the item
     */
    public String getChannel() {
        return channel;
    }

    /**
     * this method will get title from item
     * @return title of the item
     */
    public String getTitle() {
        return title;
    }

    /**
     * this method will get link from item
     * @return link of the item
     */
    public String getLink() {
        return link;
    }

    /**
     * this method will get description from item
     * @return description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * this method will get date from item
     * @return date of the item
     */
    public Date getDate() {
        return date;
    }
}