package com.gp19s2rss.rssreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

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

public class Fetch extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog = new ProgressDialog(MainActivity.getAppContext());

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading");
//        progressDialog.show(MainActivity.class);
    }

    @Override
    protected String doInBackground(String... strings) {
        String link = strings[0];
        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");
                Boolean insideItem = false;
                int eventType = xpp.getEventType();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss");
                while (eventType != XmlPullParser.END_DOCUMENT){
                    if (eventType == XmlPullParser.START_TAG){
                        if (insideItem || xpp.getName().equalsIgnoreCase("item")){
                            insideItem = true;
                            Item item = new Item();
                            item.channel = link;
                            if (xpp.getName().equalsIgnoreCase("title")){
                                item.title = xpp.nextText();
                            } else if (xpp.getName().equalsIgnoreCase("link")){
                                item.link = xpp.nextText();
                            } else if (xpp.getName().equalsIgnoreCase("description")){
                                item.description = xpp.nextText();
                            } else if (xpp.getName().equalsIgnoreCase("pubDate")){
                                item.date = dateFormat.parse(xpp.getText());
                            } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                                MainActivity.items.add(item);
                                insideItem = false;
                            }
                        }
                    }
                    eventType = xpp.next();
                }
            }

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return link;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
}
