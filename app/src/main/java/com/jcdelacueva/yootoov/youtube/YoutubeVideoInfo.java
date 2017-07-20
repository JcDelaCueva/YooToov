package com.jcdelacueva.yootoov.youtube;

import android.text.TextUtils;

public class YoutubeVideoInfo {
    private String id;
    private String title;
    private String description;
    private String duration;

    public YoutubeVideoInfo(String id, String title, String description, String duration) {
        this.id = id;
        this.duration = parseDuration(duration);
        this.description = description;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    private String parseDuration(String durationString) {
        durationString = durationString.toLowerCase();
        char[] chars = durationString.toCharArray();

        String sec = "";
        String min = "";
        String hour = "";

        String value = "";

        for (int i=0; i<chars.length; i++) {
            char c = chars[i];

            if (Character.isDigit(c)) {
                value = value.concat(String.valueOf(c));
            }

            if (c == 'h') {
                hour = value;
                value = "";
            } else if (c == 'm') {
                min = value;
                value = "";
            } else if (c == 's') {
                sec = value;
                value = "";
            }
        }

        value = "";

        if (!TextUtils.isEmpty(hour)) {
           value = String.format("%s%s hours", value, hour);
        }

        if (!TextUtils.isEmpty(min)) {
            value = String.format("%s %s mins", value, min);
        }

        if (!TextUtils.isEmpty(sec)) {
            value = String.format("%s %s secs", value, sec);
        }

        return value;
    }
}
