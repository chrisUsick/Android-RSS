package com.cu_dev.androidrss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cu_dev.androidrss.databinding.ActivityListBinding;
import com.cu_dev.androidrss.model.Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ListActivity";
    private Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        feed = this.feedFromIntent();

        ActivityListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        ListViewModel viewModel = new ListViewModel();
        viewModel.feedName.set(feed.getName());
        binding.setViewModel(viewModel);

        loadRss();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    private void loadRss() {
        new LoadFeedTask(this, feed).execute();

    }

    private Feed feedFromIntent() {
        Intent intent = getIntent();
         return (Feed)intent.getSerializableExtra(getString(R.string.extra_feed));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                openPreferences();
                break;
            case R.id.action_refresh:
                refreshList();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openPreferences() {
        Log.i(TAG, "Opening preferences");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void refreshList() {
        Toast.makeText(this, "Refreshing List", Toast.LENGTH_LONG).show();
        loadRss();
    }

    public void populateListView(SyndFeed feed) {
        ListView listView = (ListView)findViewById(R.id.feed_list);
        ArrayAdapter<SyndEntry> adapter = new SyndEntryAdapter(
                this, R.layout.rss_feed_list_item,
                selectSyndEntriesWithPreferedAmount(feed));

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private SyndEntry[] selectSyndEntriesWithPreferedAmount(SyndFeed feed) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int numberOfItems = Integer.parseInt(preferences.getString(getString(R.string.pref_number_of_items), "0"));
        numberOfItems = numberOfItems > 10 ? 10 : numberOfItems;
        return feed.getEntries().subList(0, numberOfItems).toArray(new SyndEntry[0]);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        viewDetails((SyndEntry)adapterView.getItemAtPosition(position));
    }

    private void viewDetails(SyndEntry selectedItem) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.extra_feed_detail), new FeedDetail(selectedItem));
        Log.i(TAG, "Loading details");
        startActivity(intent);
    }
}
