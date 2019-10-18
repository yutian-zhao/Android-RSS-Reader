// reference:https://blog.csdn.net/gh8609123/article/details/53495670
// CC BY-SA 4.0

package com.gp19s2rss.rssreader;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.widget.Toast.*;
/**
 * <h>the RSS reader activity <h/>
 * Control the context and listening Click of the RSS reader
 */
public class ReaderActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public void savefavs(String filename) {
        try {
            File f = new File(getExternalFilesDir(null), filename);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(MainActivity.fav);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reader_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();
        String message =intent.getStringExtra("link");
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            // Share operation
            case R.id.id_share_item:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Apps you want to share to"));
                makeText(ReaderActivity.this, "You share this article successfully.", LENGTH_SHORT).show();
                break;
            // Favorite operation
            case R.id.id_favorite_item:
                if (!MainActivity.favItem.contains(MainActivity.current_Item)) {
                    MainActivity.favItem.add(MainActivity.current_Item);

                    MainActivity.fav.add(MainActivity.current_Item.channel);
                    MainActivity.fav.add(MainActivity.current_Item.link);
                    MainActivity.fav.add(MainActivity.current_Item.description);
                    MainActivity.fav.add(MainActivity.current_Item.title);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    MainActivity.fav.add(dateFormat.format(MainActivity.current_Item.date));

                    savefavs("favs.ser");
                    makeText(ReaderActivity.this, "Add to favorite folder.", LENGTH_SHORT).show();
                } else {
                    makeText(ReaderActivity.this, "This article has been favorite.", LENGTH_SHORT).show();
                }
                break;
            // Favorite operation
            case R.id.id_unfavorite_item:
                if (MainActivity.favItem.contains(MainActivity.current_Item)) {
                    MainActivity.favItem.remove(MainActivity.current_Item);
                    //save
                    MainActivity.fav = new ArrayList<>();
                    for (Item i : MainActivity.favItem) {
                        MainActivity.fav.add(i.channel);
                        MainActivity.fav.add(i.link);
                        MainActivity.fav.add(i.description);
                        MainActivity.fav.add(i.title);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                        MainActivity.fav.add(dateFormat.format(i.date));
                    }
                    savefavs("favs.ser");
                    //refresh
                    if (MainActivity.flag == 1) {
                        MainActivity.itemAdapter = new ItemAdapter(MainActivity.context, R.layout.list_view_items, MainActivity.favItem);
                        MainActivity.listView.setAdapter(MainActivity.itemAdapter);
                    }
                    makeText(ReaderActivity.this, "Delete from favorite folder.", LENGTH_SHORT).show();
                } else {
                    makeText(ReaderActivity.this, "This article is not in the favorite folder yet~~.", LENGTH_SHORT).show();
                }
                break;
            // Facebook share
//            case R.id.id_FBshare_item:
//                ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentUrl(Uri.parse(message))
//                    .build();
//                if (ShareDialog.canShow(ShareLinkContent.class)){
//                    shareDialog.show(linkContent);
//                }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_reader);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        WebView webView = findViewById(R.id.webview);

        Intent intent = getIntent();
        String info = intent.getStringExtra("link");
        webView.loadUrl(info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
