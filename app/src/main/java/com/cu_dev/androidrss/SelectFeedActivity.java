package com.cu_dev.androidrss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cu_dev.androidrss.model.Feed;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SelectFeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_feed);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initFeedSelector();
    }

    private void initFeedSelector() {
        Spinner spinner = (Spinner)findViewById(R.id.feed_selector);
        ArrayAdapter<Feed> adapter = new ArrayAdapter<Feed>(
                this, android.R.layout.simple_spinner_item,
                loadFeeds().toArray(new Feed[0])
        );
        spinner.setAdapter(adapter);

        setDefaultFeed(spinner);

        spinner.setOnItemSelectedListener(this);
    }

    private void setDefaultFeed(Spinner spinner) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultFeedTopic = preferences.getString(getString(R.string.pref_default_feed), "");
        int position = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            Feed feed = (Feed) spinner.getAdapter().getItem(i);
            if (feed.getTopic().equals(defaultFeedTopic)) {
                position = i;
                break;
            }
        }
        spinner.setSelection(position);
    }

    private ArrayList<Feed> loadFeeds() {

        ArrayList<Feed> feeds = new ArrayList<>();

        Resources res = getResources();
        String[] topics = res.getStringArray(R.array.pref_feed_entry_values);
        String[] names = res.getStringArray(R.array.pref_feed_entries);
        for (int i = 0; i < names.length; i++) {
            String name = names[i],
                    topic = topics[i];
            feeds.add(new Feed(name, topic));
        }
        return feeds;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Feed feed = (Feed)adapterView.getSelectedItem();
        Log.i(TAG, "item selected: " + feed.getName());

    }

    private void startListActivity(Feed feed) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(getString(R.string.extra_feed), feed);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.i(TAG, "Nothing selected");
    }

    public void loadFeed(View view) {
        Log.i(TAG, "Loading feed");
        Spinner spinner = (Spinner)findViewById(R.id.feed_selector);
        Feed feed = (Feed)spinner.getSelectedItem();
        startListActivity(feed);
    }
}
