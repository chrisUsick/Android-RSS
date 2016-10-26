package com.cu_dev.androidrss;

import android.os.AsyncTask;
import android.util.Log;

import com.cu_dev.androidrss.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;


import java.net.URL;

/**
 * Created by chris on 07-Oct-16.
 */
public class LoadFeedTask extends AsyncTask<Void, Void, SyndFeed> {
    private static final String TAG = "LoadFeedTask";
    String uri;
    ListActivity context;
    public LoadFeedTask(ListActivity context, Feed feed) {
        uri = feed.getUri();
        this.context = context;
    }
    @Override
    protected SyndFeed doInBackground(Void... voids) {
        try {
            Log.i(TAG, "Starting to fetch document");
            URL url = new URL(uri);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            return feed;
        } catch (Exception e) {
            Log.e(TAG, "Error getting feed", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(SyndFeed rssFeed) {
        super.onPostExecute(rssFeed);
        Log.i(TAG, "Got feed " + rssFeed.getTitle());
        context.populateListView(rssFeed);
    }
}
