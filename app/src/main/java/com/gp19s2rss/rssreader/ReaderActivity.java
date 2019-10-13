package com.gp19s2rss.rssreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import java.util.HashSet;
import java.util.Set;

import static android.widget.Toast.*;

public class ReaderActivity extends AppCompatActivity {
// reference:https://blog.csdn.net/gh8609123/article/details/53495670
// CC BY-SA 4.0

    Uri rawUri;
    Set<Uri> favorite_folder = new HashSet<>();

    ReaderActivity(){};

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        Button share = findViewById(R.id.share);
        Button favorite = findViewById(R.id.favorite);
        Button unread = findViewById(R.id.unread);
        WebView webView = (WebView) findViewById(R.id.webview);

        // web view
        Intent intent = getIntent();
        String info = intent.getStringExtra("link");
        webView.loadUrl(info);

        // Share operation
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeText(ReaderActivity.this, "You share this article successfully.", LENGTH_SHORT).show();
            }
        });

        // Favorite operation
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favorite_folder.contains(rawUri)) {
                    favorite_folder.add(rawUri);
                    makeText(ReaderActivity.this, "Add to favorite folder.", LENGTH_SHORT).show();
                } else {
                    makeText(ReaderActivity.this, "This article has been favorite.", LENGTH_SHORT).show();
                }
            }
        });

        // Unread operation
        unread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeText(ReaderActivity.this, "Mark as unread.", LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
