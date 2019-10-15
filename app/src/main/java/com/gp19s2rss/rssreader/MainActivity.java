package com.gp19s2rss.rssreader;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Uri rawUri;
    ArrayList<String> links = new ArrayList<>();
    public static Context context;
    public static ArrayList<Item> items = new ArrayList<>();
    public static ItemAdapter itemAdapter;
    public static ListView listView;

    public ArrayList<String> loadLinks(String filename) {
        try {
            File f = new File(getExternalFilesDir(null), filename);
            if (f.exists() && f.canRead() && f.length() != 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                links = (ArrayList<String>) ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return links;
    }

    public void saveLinks(String filename) {
        try {
            File f = new File(getExternalFilesDir(null), filename);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(links);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                            if (!links.contains(rawUri.toString())) {
                                links.add(rawUri.toString());
                                Toast.makeText(MainActivity.this,
                                        "Subscribe Successfully.",
                                        Toast.LENGTH_SHORT).show();
                                saveLinks("links.ser");
                                refresh();
                                Valid_URI_Action(rawUri);
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Uri already exists.",
                                        Toast.LENGTH_SHORT).show();
//                                itemAdapter = new ItemAdapter(MainActivity.this, R.layout.list_view_items, items);
//                                listView.setAdapter(itemAdapter);//
//                                itemAdapter.notifyDataSetChanged();
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

    //?web user, browser/ scroll, load, auto sort/ notify/ UI

    // rust server
    // transfer parse html to rich text (viewer)

    // (load)(icon)
    // sort arraylist of link class time(old/ new) feed name (unread/recently read/favourite) search

    // add - folder - refresh

    //treat navigation drawer as listview(both clickable); setadapter throw null pointer exception; How to correctly implement BaseAdapter.notifyDataSetChanged() in Android; asynctask execute;
    // asynctask notify; list view

    // Reaction to the valid uri input.
    protected void Valid_URI_Action(Uri uri) {
        //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //startActivity(intent);
    }

    public void refresh() {
        links = loadLinks("links.ser");
        Fetch fetchTask = new Fetch();
        items = new ArrayList<>();
        fetchTask.execute(links.toArray(new String[links.size()]));
        // not empty, sorted by time automatically
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        // To represent items inside a list_Viewer
        itemAdapter = new ItemAdapter(this, R.layout.list_view_items, items);
        listView = (ListView) findViewById(R.id.list_view);

        listView.setAdapter(itemAdapter);
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Uri targetUri = Uri.parse(links.get(position));
                //Intent intent = new Intent(Intent.ACTION_VIEW,targetUri);
                //startActivity(intent);
                String url = items.get(position).link;
                Intent intent = new Intent(getAppContext(), ReaderActivity.class);
                intent.putExtra("link", url);
//                intent.setClass(MainActivity.this, ReaderActivity.class);
                startActivity(intent);
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

        saveLinks("links.ser"); // clean
        links = loadLinks("links.ser");
        Fetch fetchTask = new Fetch();
        for (String s : links) {
            fetchTask.execute(s);
        }
    }

    public static Context getAppContext() {
        return context;
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
        if (id == R.id.sort_by_recent) {

            // Sort all items by ordering date.
            Collections.sort(items, new Comparator<Item>() {
                @Override
                public int compare(Item i1, Item i2) {
                    return i2.getDate().compareTo(i1.getDate());
                }
            });
            itemAdapter = new ItemAdapter(context, R.layout.list_view_items, items);
            listView.setAdapter(itemAdapter);

            Toast.makeText(MainActivity.this,
                    "Sort by time recently successfully.",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_early) {
            // Sort all items by ordering date.
            Collections.sort(items, new Comparator<Item>() {
                @Override
                public int compare(Item i1, Item i2) {
                    return i1.getDate().compareTo(i2.getDate());
                }
            });
            itemAdapter = new ItemAdapter(context, R.layout.list_view_items, items);
            listView.setAdapter(itemAdapter);

            Toast.makeText(MainActivity.this,
                    "Sort by time early successfully.",
                    Toast.LENGTH_SHORT).show();
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
        } else if (id == R.id.nav_management) {
            Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Websites) {

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
