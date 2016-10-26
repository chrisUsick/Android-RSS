package com.cu_dev.androidrss;

import android.databinding.ObservableField;
import android.text.Html;
import android.text.Spanned;

import com.rometools.rome.feed.synd.SyndEntry;

import java.io.Serializable;

/**
 * Created by chris on 07-Oct-16.
 */
public class FeedDetail implements Serializable {
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> link = new ObservableField<>();
    public FeedDetail(SyndEntry entry) {
        title.set(entry.getTitle());
        description.set(entry.getDescription().getValue());
        link.set(entry.getLink());
    }
}
