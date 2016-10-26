package com.cu_dev.androidrss;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rometools.rome.feed.synd.SyndEntry;

import java.util.Date;

/**
 * Created by chris on 09-Oct-16.
 */
public class SyndEntryAdapter extends ArrayAdapter<SyndEntry> {
    private static final String TAG = "SyndEntryAdapter";
    private final int layoutResourceId;

    public SyndEntryAdapter(Context context, int textViewResourceId, SyndEntry[] objects) {
        super(context, textViewResourceId, objects);
        layoutResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            SyndEntry item = getItem(position);
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(layoutResourceId, null);
            } else {
                v = convertView;
            }
            TextView title = (TextView) v.findViewById(R.id.rss_feed_list_title);
            TextView pubDate = (TextView) v.findViewById(R.id.rss_feed_list_pubDate);
            title.setText(item.getTitle());
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean showPubDate = preferences.getBoolean(getContext().getString(R.string.pref_show_pub_date), false);
            if (showPubDate) {
                pubDate.setText(formatPubDate(item.getPublishedDate()));
            }
            return v;
        } catch (Exception e) {
            Log.e(TAG, "Error getting view", e);
            return null;
        }
    }

    private String formatPubDate(Date publishedDate) {
        java.text.DateFormat format = DateFormat.getMediumDateFormat(getContext());
        return format.format(publishedDate);
    }
}
