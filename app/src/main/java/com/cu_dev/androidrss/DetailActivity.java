package com.cu_dev.androidrss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.cu_dev.androidrss.databinding.ActivityDetailBinding;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loadRssEntry();
    }

    private void loadRssEntry() {
        Intent intent = getIntent();
        FeedDetail feedDetail = (FeedDetail)intent.getSerializableExtra(getString(R.string.extra_feed_detail));
        initBinding(feedDetail);
        displayDescription(feedDetail);
        Log.i(TAG, "Loading details of: " + feedDetail.title.get());
        Log.i(TAG, "Description\n" + feedDetail.description.get());
    }

    private void displayDescription(FeedDetail feedDetail) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useWebView = preferences.getBoolean(getString(R.string.pref_webview_or_parse_html), true);
        if (useWebView) {
            setWebView(feedDetail.description.get());
        } else {
            TextView descriptionView = (TextView) findViewById(R.id.detail_description);
            descriptionView.setText(Html.fromHtml(feedDetail.description.get().replaceAll("<img.+?>", "")));
        }

    }

    private void setWebView(String description) {
        WebView wv = (WebView)findViewById(R.id.description_webview);
        wv.getSettings().setDomStorageEnabled(true);
        wv.loadData(description, "text/html", null);
    }

    private void initBinding(FeedDetail feedDetail) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setViewModel(feedDetail);
        TextView linkView = (TextView) findViewById(R.id.link);
        linkView.setMovementMethod(LinkMovementMethod.getInstance());
        String textValue = "<a href='" + feedDetail.link.get() + "'>Link</a>";
        Log.d(TAG, "Link Value: " + textValue);
        linkView.setText(Html.fromHtml(textValue));
    }
}
