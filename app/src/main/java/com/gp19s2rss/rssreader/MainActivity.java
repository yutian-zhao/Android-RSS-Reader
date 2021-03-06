package com.gp19s2rss.rssreader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * <h1> MainActivity of APP </h1>
 * This is default activity of this RSS application
 *
 * @version 1.0
 * @since 2019-10-08th
 * Reference:
 *  -basic learning : https://blog.csdn.net/
 *     --licence  CC BY-SA 4.0
 *  -Feed Validation Service: https://validator.w3.org/feed/
 *     --licence  CC BY-SA 4.0
 *  -SwipeView Package: https://github.com/baoyongzhang/SwipeMenuListView
 *     --licence  CC BY-SA 4.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<String> links = new ArrayList<>();
    public static ArrayList<String> fav = new ArrayList<>();
    public static ArrayList<String> list = new ArrayList<>();
    public static ArrayList<Item> favItem = new ArrayList<>();
    public static Item current_Item = new Item();
    public static Context context;
    public static ArrayList<Item> items = new ArrayList<>();
    public static ItemAdapter itemAdapter;
    public static ListView listView;
    public static int flag = 0;
    public static ArrayList<Item> linkItems = new ArrayList<>();
    public static SwipeMenuListView swipeView;
    public static ArrayAdapter adapter;
    // Using this Regular expression to check a string is a valid link or not.
    public static Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)" +
            "(([A-Za-z0-9-~]+).)+" + "([A-Za-z0-9-~\\/])+$");

    /**
     * This method generates a array list of all user's links
     *
     * @param filename the file stores all links
     * @return links array
     */
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

    /**
     * This method generates a array list of  user's favorite links
     *
     * @param filename the file stores all favorite links
     * @return favorite links array
     */
    public ArrayList<String> loadfavs(String filename) {
        try {
            File f = new File(getExternalFilesDir(null), filename);
            if (f.exists() && f.canRead() && f.length() != 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                fav = (ArrayList<String>) ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return fav;
    }

    /**
     * This method stores links to user's subscription
     *
     * @param filename position to save links
     */
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

    /**
     * This method stores user's favorite links to favorite folder
     *
     * @param filename file to save favorite links
     */
    public void savefavs(String filename) {
        try {
            File f = new File(getExternalFilesDir(null), filename);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(fav);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks if a uri contains RSS tag
     *
     * @param uri input URI want to subscribe
     * @return whether uri has valid rss or not
     */
    public static  boolean validateRss(String uri) {
        if (!pattern.matcher(uri).matches()) return false;
        if (uri.contains(".xml")) return true;
        if (uri.contains("/feed")) return true;
        if (uri.contains("/rss")) return true;
        return false;
    }

    /**
     * This method generates a text box. Users can type Uri on it for subscribing.
     * After receiving a Uri,the method makes initial judgement of the RSS Uri's validity.
     * It gives feedback to users and deals with the Uri after judgement.
     */
    private void showTypeURI() {
        final EditText addWindow = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("Type the source link you want to read.").setView(addWindow);
        inputDialog.setPositiveButton("Subscribe",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uri_string = addWindow.getText().toString();
                        if (validateRss(uri_string)) {
                            // The input is a valid link.
                            Uri rawUri = Uri.parse(uri_string);
                            if (!links.contains(rawUri.toString())) {
                                links.add(rawUri.toString());
                                Toast.makeText(MainActivity.this,
                                        "Subscribe Successfully.",
                                        Toast.LENGTH_SHORT).show();
                                saveLinks("links.ser");
                                refresh();
                                refreshSwipeView(links);
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Uri already exists.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Invalid Rss.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    /**
     * Reload links from aimed file
     * inorder to refresh all items on the list view
     */
    public void refresh() {
        links = loadLinks("links.ser");
        Fetch fetchTask = new Fetch();
        items = new ArrayList<>();
        fetchTask.execute(links.toArray(new String[links.size()]));
        // not empty, sorted by time automatically
    }

    /**
     * This method controls the click listening
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        // To refresh the list view by click refresh
        FloatingActionButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            /**
             * This method refreshes the application, if users
             * click refresh button.
             * @param v
             */
            @Override
            public void onClick(View v) {
                refresh();
                // Sort all items by ordering date.
                Toast.makeText(MainActivity.this,
                        "Refresh the page.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // To represent items inside a list_Viewer
        if (flag == 0) {
            itemAdapter = new ItemAdapter(this, R.layout.list_view_items, items);
            listView = findViewById(R.id.list_view);
        } else if (flag == 1) {
            itemAdapter = new ItemAdapter(this, R.layout.list_view_items, favItem);
            listView = findViewById(R.id.list_view);
        } else {
            itemAdapter = new ItemAdapter(this, R.layout.list_view_items, linkItems);
            listView = findViewById(R.id.list_view);
        }

        listView.setAdapter(itemAdapter);
        final AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            /**
             * This method judge the attribute of the items, and design where to store the Uri
             * @param parent item's parent
             * @param view   item's view
             * @param position item's position
             * @param id item's id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url;
                if (flag == 0) {
                    url = items.get(position).link;
                    current_Item = items.get(position);
                } else if (flag == 1) {
                    url = favItem.get(position).link;
                    current_Item = favItem.get(position);
                } else {
                    url = linkItems.get(position).link;
                    current_Item = linkItems.get(position);
                }
                Intent intent = new Intent(getAppContext(), ReaderActivity.class);
                intent.putExtra("link", url);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(clickListener);


        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            /**
             * When users click add button, show the text box for subscribing.
             * @param view
             */
            @Override
            public void onClick(View view) {
                // link collection
                showTypeURI();
            }
        });

        FloatingActionButton manual = findViewById(R.id.manual);
        manual.setOnClickListener(new View.OnClickListener() {
            /**
             * When users click add button, show the text box for manual.
             * @param view
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(MainActivity.this);
                inputDialog.setTitle("Manual");
                inputDialog.setMessage("Click add button to add new RSS feed.\n\nCLick top-right button to sort.\n\nClick refresh button to refresh.\n\nManage subscription in navigation slide.\n\nClick top-left button in reading page to share or mark as favourite.\n");
                AlertDialog manual = inputDialog.create();
                manual.show();
            }
        });
        /**
         * Following progress stores the valid Rss Uri,
         * represents the Items in the main list viewer,
         * and represents the Uri and favorite in the drawer list.
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        saveLinks("links.ser"); // clean
//        savefavs("favs.ser"); // clean
        links = loadLinks("links.ser");
        fav = loadfavs("favs.ser");
        //load my favs
        for (int i = 0; i < fav.size(); i += 5) {
            Item item = new Item();
            item.channel = fav.get(i);
            item.link = fav.get(i + 1);
            item.description = fav.get(i + 2);
            item.title = fav.get(i + 3);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            try {
                item.date = dateFormat.parse(fav.get(i + 4));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!favItem.contains(item))
                favItem.add(item);
        }

        Fetch fetchTask = new Fetch();
        for (String s : links) {
            fetchTask.execute(s);
        }

        list = new ArrayList<>();
        list.add("All");
        list.add("Favourite");
        for (String s : links) {
            list.add(s);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        swipeView = findViewById(R.id.swipeView);
        swipeView.setAdapter(adapter);
        swipeView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        final AdapterView.OnItemClickListener swipeViewClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    flag = 0;
                    refresh();
                } else if (position == 1) {
                    flag = 1;
                    itemAdapter = new ItemAdapter(context, R.layout.list_view_items, favItem);
                    listView.setAdapter(itemAdapter);

                    Toast.makeText(MainActivity.this,
                            "Show the favourite folder",
                            Toast.LENGTH_SHORT).show();
                } else {
                    flag = 2;
                    linkItems.clear();
                    for (Item i : items) {
                        if (i.channel.equals(list.get(position))) {
                            linkItems.add(i);
                        }
                    }
                    itemAdapter = new ItemAdapter(context, R.layout.list_view_items, linkItems);
                    listView.setAdapter(itemAdapter);

                    Toast.makeText(MainActivity.this,
                            list.get(position),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        swipeView.setOnItemClickListener(swipeViewClickListener);


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            /**
             * editing the drawer appearance and functions
             * @param menu
             */
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        swipeView.setMenuCreator(creator);
        swipeView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            /**
             * This method set the functions of "favorite" and "all" in the drawer list
             *
             * @param position
             * @param menu
             * @param index
             * @return boolean to complete the click listening
             */
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    /**
                     * The following progresses make final checking of the RSS Uri's validity.
                     */
                    case 0:
                        if (position == 0) {
                            list.clear();
                            list.add("All");
                            list.add("Favourite");
                            links.clear();
                            saveLinks("links.ser");
                            MainActivity.adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list);
                            swipeView.setAdapter(MainActivity.adapter);
                            refresh();
                        } else if (position == 1) {
                            favItem = new ArrayList<>();
                            savefavs("favs.ser");
                            if (flag == 1) {
                                itemAdapter = new ItemAdapter(context, R.layout.list_view_items, favItem);
                                listView.setAdapter(itemAdapter);
                            }
                        } else {
                            MainActivity.links.remove(list.get(position));
                            saveLinks("links.ser");
                            MainActivity.list.remove(position);
                            MainActivity.adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list);
                            MainActivity.swipeView.setAdapter(MainActivity.adapter);
                            refresh();
                            flag = 0;
                            itemAdapter = new ItemAdapter(context, R.layout.list_view_items, items);
                            listView.setAdapter(itemAdapter);
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    /**
     * Adding the subscribed Uri, "favorite" and "ALL" to drawer list.
     *
     * @param input users' subscriptions
     */
    public void refreshSwipeView(ArrayList<String> input) {
        list = new ArrayList<>();
        list.add("All");
        list.add("Favourite");
        for (String s : input) {
            list.add(s);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        swipeView.setAdapter(adapter);

    }


    /**
     * This method sorts the Items by Item.date with New order
     *
     * @param items items needed to sort
     */
    public void sortByNew(ArrayList<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                return i2.getDate().compareTo(i1.getDate());
            }
        });
        itemAdapter = new ItemAdapter(context, R.layout.list_view_items, items);
        listView.setAdapter(itemAdapter);

        Toast.makeText(MainActivity.this,
                "Sort by new successfully.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * This method sorts the Items by Item.date with Old order
     *
     * @param items items needed to sort
     */
    public void sortByOld(ArrayList<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                return i1.getDate().compareTo(i2.getDate());
            }
        });
        itemAdapter = new ItemAdapter(context, R.layout.list_view_items, items);
        listView.setAdapter(itemAdapter);

        Toast.makeText(MainActivity.this,
                "Sort by old successfully.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * @return represent the context of this
     */
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

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu
     * @return boolean to complete the click listening
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return super.onOptionsItemSelected(item);
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (flag == 0) {
            if (id == R.id.sort_by_new) {
                sortByNew(items);
                return true;
            } else if (id == R.id.sort_by_old) {
                sortByOld(items);
                return true;
            }
        } else if (flag == 1) {
            if (id == R.id.sort_by_new) {
                sortByNew(favItem);
                return true;
            } else if (id == R.id.sort_by_old) {
                sortByOld(favItem);
                return true;
            }
        } else {
            if (id == R.id.sort_by_new) {
                sortByNew(linkItems);
                return true;
            } else if (id == R.id.sort_by_old) {
                sortByOld(linkItems);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
