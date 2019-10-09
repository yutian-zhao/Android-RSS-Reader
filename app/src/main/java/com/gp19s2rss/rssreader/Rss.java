package com.gp19s2rss.rssreader;

public class Rss {
    private String tile;
    private String link;
    private String description;
    private int id;

    public Rss(String tile, String link, String description, int id) {
        this.tile = tile;
        this.link = link;
        this.description = description;
        this.id = id;
    }

    public String getTile() {
        return tile;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
