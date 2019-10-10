package com.gp19s2rss.rssreader;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Uri rawUri;
    Set<Uri> Subscribed_Uri = new HashSet<>();
    List<String> data = new ArrayList<>();

    private void showtypeURI() {
        final EditText addWindow = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("Type the source link you want to read.").setView(addWindow);
        inputDialog.setPositiveButton("Subscribe",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uri_string = addWindow.getText().toString();
                        if (pattern.matcher(uri_string).matches()) {
                            // The input is a valid link.
                            rawUri = Uri.parse(uri_string);
                            if (!Subscribed_Uri.contains(rawUri)) {
                                Subscribed_Uri.add(rawUri);
                                data.add(rawUri.toString());
                                Toast.makeText(MainActivity.this,
                                        "Subscribe Successfully.",
                                        Toast.LENGTH_SHORT).show();
                                Valid_URI_Action(rawUri);
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Uri already exists.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Invalid Uri.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    // memory(rss array on top, favourite link xml/ link class array output) - nav - refresh - list
    // valid
    // list
    // show
    // refresh
    // folder (icon)

    // (load)(icon)
    // sort arraylist of link class time(old/ new) feed name (unread/recently read/favourite) search

    // add - folder - refresh

    // Reaction to the valid uri input.
    protected void Valid_URI_Action(Uri uri) {
        //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // To represent items inside a list_Viewer
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Uri targetUri = Uri.parse(data.get(position));
                //Intent intent = new Intent(Intent.ACTION_VIEW,targetUri);
                //startActivity(intent);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ReaderActivity.class);
                startActivityForResult(intent, 0);
            }
        };
        listView.setOnItemClickListener(clickListener);

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // link collection
                showtypeURI();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // press
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Uri uri = Uri.parse("https://www.baidu.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent); // usable
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Using this Regular expression to check a string is a valid link or not.
    Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)" +
            "(([A-Za-z0-9-~]+).)+" + "([A-Za-z0-9-~\\/])+$");
}
