package com.gp19s2rss.rssreader;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

/**
 * <h1>Fetch feeds information </h1>
 * This class will fetch all information of feeds from a RSS URI.
 * And refresh local storage document for updating.
 * @version 1.0
 * @since 2019-10-08th
 */
public class Fetch extends AsyncTask<String, Integer, String> {

    ProgressDialog progressDialog = new ProgressDialog(MainActivity.getAppContext());

    /**
     * It will show notice of "loading" when the activity build at background
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading");
//        progressDialog.show(MainActivity.class);
    }


    /**
     * This method will fetch the feed information
     * from the links witch are called by users.
     * The feed's information will be storage in Item
     * (title,date,description,link)
     * All important functions will be done during the progress.
     *
     * @param links The links added by users
     * @return Blanket
     */

    @Override
    protected String doInBackground(String... links) {
        Item item = new Item();
        for (String link : links) {
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    XmlPullParserFactory xppFactory = XmlPullParserFactory.newInstance();
                    xppFactory.setNamespaceAware(false);
                    XmlPullParser xpp = xppFactory.newPullParser();
                    xpp.setInput(httpURLConnection.getInputStream(), "UTF-8");

                    boolean insideItem = false;
                    int eventType = xpp.getEventType();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("item")) {
                                insideItem = true;
                                item.channel = link;
                            } else if (xpp.getName().equalsIgnoreCase("title")) {
                                if (insideItem) {
                                    item.title = xpp.nextText();
                                }
                            } else if (xpp.getName().equalsIgnoreCase("link")) {
                                if (insideItem) {
                                    item.link = xpp.nextText();
                                }
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                if (insideItem) {
                                    item.description = xpp.nextText();
                                }
                            } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                if (insideItem) {
                                    item.date = dateFormat.parse(xpp.nextText());
                                }
                            }
                        }
                        if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                            if (!MainActivity.items.contains(item)) {
                                MainActivity.items.add(item);
                            }
                            insideItem = false;
                            item = new Item();
                        }
                        eventType = xpp.next();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * This method will refresh the documents
     * after fetching from feeds
     *
     * @param s s is only a formal param, ensure the main produce run smoothly
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (MainActivity.flag == 0) {
            Collections.sort(MainActivity.items, new Comparator<Item>() {
                /**
                 * this method will sort the items by date
                 * @param i1 First item (Item storage feed information)
                 * @param i2 Second item
                 * @return sorted items
                 */
                @Override
                public int compare(Item i1, Item i2) {
                    return i2.getDate().compareTo(i1.getDate());
                }
            });
            MainActivity.itemAdapter = new ItemAdapter(MainActivity.context, R.layout.list_view_items, MainActivity.items);
            MainActivity.listView.setAdapter(MainActivity.itemAdapter);
        } else if (MainActivity.flag == 1) {
            Collections.sort(MainActivity.favItem, new Comparator<Item>() {
                /**
                 * this method will sort the items by date
                 * @param i1 First item (Item storage feed information)
                 * @param i2 Second item
                 * @return sorted items
                 */
                @Override
                public int compare(Item i1, Item i2) {
                    return i2.getDate().compareTo(i1.getDate());
                }
            });
            MainActivity.itemAdapter = new ItemAdapter(MainActivity.context, R.layout.list_view_items, MainActivity.favItem);
            MainActivity.listView.setAdapter(MainActivity.itemAdapter);
        } else {
            Collections.sort(MainActivity.linkItems, new Comparator<Item>() {
                /**
                 * this method will sort the items by date
                 * @param i1 First item (Item storage feed information)
                 * @param i2 Second item
                 * @return sorted items
                 */
                @Override
                public int compare(Item i1, Item i2) {
                    return i2.getDate().compareTo(i1.getDate());
                }
            });
            MainActivity.itemAdapter = new ItemAdapter(MainActivity.context, R.layout.list_view_items, MainActivity.linkItems);
            MainActivity.listView.setAdapter(MainActivity.itemAdapter);
        }
        progressDialog.dismiss();
    }
}

