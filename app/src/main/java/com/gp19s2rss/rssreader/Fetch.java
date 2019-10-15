package com.gp19s2rss.rssreader;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fetch extends AsyncTask<String, Integer, String> {

    ProgressDialog progressDialog = new ProgressDialog(MainActivity.getAppContext());

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading");
//        progressDialog.show(MainActivity.class);
    }

    @Override
    protected String doInBackground(String... strings) {
        Item item = new Item();
        for (String link : strings) {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == 200) {
                    InputStream response = connection.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(connection.getInputStream(), "UTF-8");
                    boolean insideItem = false;
                    int eventType = xpp.getEventType();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss");
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("item")) {
                                insideItem = true;
                                item.channel = link;
                            } else if (xpp.getName().equalsIgnoreCase("title")) {
                                if (insideItem) {
                                    String st = xpp.nextText();
                                    item.title = st;
                                }
                            } else if (xpp.getName().equalsIgnoreCase("link")) {
                                if (insideItem) {
                                    String st = xpp.nextText();
                                    item.link = st;
                                }
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                if (insideItem) {
                                    String st = xpp.nextText();
                                    item.description = st;
                                }
                            } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                if (insideItem) {
                                    // TODO time get wrong
                                    String st = xpp.nextText();
                                    item.date = dateFormat.parse(st);
                                }
                            }
                        }
                        if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                            MainActivity.items.add(item);
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.itemAdapter = new ItemAdapter(MainActivity.context, R.layout.list_view_items, MainActivity.items);
        MainActivity.listView.setAdapter(MainActivity.itemAdapter);
        progressDialog.dismiss();
    }
}

