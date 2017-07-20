package com.jcdelacueva.yootoov.models;

import com.parse.ParseClassName;

@ParseClassName("Converts")
public class Convert extends BaseParseObject {
    private static final String URL = "url";
    private static final String USER = "User";
    private static final String TITLE = "title";
    private static final String DURATION = "duration";
    private static final String STATUS = "status";

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_DOWNLOADED = "downloaded";

    //Setters
    public void setUrl(String url) {
        put(URL, url);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public void setTitle(String title) {
        put(TITLE, title);
    }

    public void setDuration(String duration) {
        put(DURATION, duration);
    }

    public void setStatus(String status) {
        put(STATUS, status);
    }

    //Getters
    public String getUrl() {
        return getString(URL);
    }

    public User getUser() {
        return (User)getParseObject(USER);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public String getDuration() {
        return getString(DURATION);
    }

    public String getStatus() {
        return getString(STATUS);
    }

    //QUERY

}
