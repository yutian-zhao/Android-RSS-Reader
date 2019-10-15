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

import java.util.HashSet;
import java.util.Set;

import static android.widget.Toast.*;

public class ReaderActivity extends AppCompatActivity {

    Uri rawUri;
    Set<Uri> favorite_folder = new HashSet<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reader_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            // Share operation
            case R.id.id_share_item:
                makeText(ReaderActivity.this, "You share this article successfully.", LENGTH_SHORT).show();
                break;
            // Favorite operation
            case R.id.id_favorite_item:
                if (!favorite_folder.contains(rawUri)) {
                    favorite_folder.add(rawUri);
                    makeText(ReaderActivity.this, "Add to favorite folder.", LENGTH_SHORT).show();
                } else {
                    makeText(ReaderActivity.this, "This article has been favorite.", LENGTH_SHORT).show();
                }
                break;
            // Unread operation
            case R.id.id_unread_item:
                makeText(ReaderActivity.this, "Mark as unread.", LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        WebView webView = (WebView) findViewById(R.id.webview);

        // web view
        Intent intent = getIntent();
        String info = intent.getStringExtra("link");
        webView.loadUrl(info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
