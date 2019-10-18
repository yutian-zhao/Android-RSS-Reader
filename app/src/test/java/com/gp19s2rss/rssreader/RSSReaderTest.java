package com.gp19s2rss.rssreader;

import android.app.Service;
import android.content.Context;
import android.os.Handler;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RSSReaderTest{
    //  extends InstrumentationTestCase

    /**
     Most of out code is comnined with UI. It's really hard to test AsyncTask, which far beyond the
     scope of JUnit4. We eventual give up making more tests.
     Please take this into consideration. Thanks
     */

    ArrayList<String> sampleLinks= new ArrayList<>();

    @ Before
    public void createSampleLinks(){
        sampleLinks.add("http://feeds.bbci.co.uk/news/world/rss.xml");
        sampleLinks.add("http://feeds.reuters.com/Reuters/worldNews");
        sampleLinks.add("http://sspai.com/feed");

    }

    @Test
    public void validRSSTest(){
        for (String s : sampleLinks){
            assertTrue(s + "should be true but return false", MainActivity.validateRss(s));
        }
        assertFalse(" " + "should be true but return false", MainActivity.validateRss(" "));
        assertFalse(" " + "should be true but return false", MainActivity.validateRss(" http"));

    }
//
//    @Test
//    public void testSomeAsynTask () throws Throwable {
//        // create  a signal to let us know when our task is done.
//        final CountDownLatch signal = new CountDownLatch(1);
//        final Fetch myTask = new Fetch();
//        myTask.signal = signal;
//        myTask.signalSet = true;
//        // Execute the async task on the UI thread! THIS IS KEY!
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                myTask.execute(sampleLinks.toArray(new String[sampleLinks.size()]));
//            }
//        });
//
//        /* The testing thread will wait here until the UI thread releases it
//         * above with the countDown() or 30 seconds passes and it times out.
//         */
//        signal.await(30, TimeUnit.SECONDS);
//
//        // The task is done, and now you can assert some things!
//        assertTrue("Happiness", true);
//    }

}