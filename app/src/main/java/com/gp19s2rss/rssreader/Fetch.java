package com.gp19s2rss.rssreader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * <h1>Fetch feeds information </h1>
 * This class will fetch all information of feeds from a RSS URI.
 * And refresh local storage document for updating.
 *
 * @version 1.0
 * @since 2019-10-10th
 */
public class Fetch extends AsyncTask<String, Integer, String> {

    /**
     * This method will fetch the feed information
     * from the links which are called by users.
     * The feed's information will be stored in Item
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
                if (valid_Rss(link)){
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
                }}
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
                 * @param i1 First item (Item stores feed information)
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
                 * @param i1 First item (Item stores feed information)
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
                 * @param i1 First item (Item stores feed information)
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
    }

    public static boolean valid_Rss(String uri) {
        //Strictly check the validity of a rss feed. Slow.
        try {
            URL url = new URL("https://validator.w3.org/feed/check.cgi?url=".concat(uri));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream is = httpURLConnection.getInputStream();
                Scanner s = new Scanner(is).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                return result.contains("Congratulations");
            } else {
                Toast.makeText(MainActivity.context,
                        "Connection error.",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (MalformedURLException e){
            Toast.makeText(MainActivity.context,
                    "Rss is invalid",
                    Toast.LENGTH_SHORT).show();
            return false;
        } catch (IOException e){
            Toast.makeText(MainActivity.context,
                    "IOException",
                    Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception e){
            Toast.makeText(MainActivity.context,
                    "Unknown Exception",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
}