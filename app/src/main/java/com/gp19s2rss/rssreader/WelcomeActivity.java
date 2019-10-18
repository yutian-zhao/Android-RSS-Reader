package com.gp19s2rss.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * This activity show the welcome page for users
 * idea reference: https://stackoverflow.com/questions/34972927/welcome-activity-in-android
 * Licence:  CC BY-SA 4.0
 * @version 1.0
 * @since 2019-10-17th
 */
public class WelcomeActivity extends AppCompatActivity {
    /**
     * Creating a new page for welcome
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            /**
             * Time counter for ending welcome activity,and running main activity.
             */
            @Override
            public void run() {

                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        }, 4000);
    }


}
