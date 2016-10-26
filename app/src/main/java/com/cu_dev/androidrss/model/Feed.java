package com.cu_dev.androidrss.model;

import java.io.Serializable;

/**
 * Created by chris on 07-Oct-16.
 */
public class Feed implements Serializable {
    private String name;
    private String topic;

    public Feed(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getUri() {
        String topicParam = topic.length() == 0 ? "" : "&topic=" + topic;
        return "https://news.google.com/news?cf=all&hl=en&pz=1&ned=ca&output=rss" + topicParam;
    }
}
