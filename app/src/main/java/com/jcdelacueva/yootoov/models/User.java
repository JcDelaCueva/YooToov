package com.jcdelacueva.yootoov.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by robin on 17/07/2017.
 */

@ParseClassName("_User")
public class User extends ParseUser {
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CONVERTS = "converts";
    private static final String CONVERT_COUNT = "convertCount";

    public String getUsername() {
        return getString(USERNAME);
    }

    public String getEmail() {
        return getString(USERNAME);
    }

    public int getConvertsCount() {
        return getInt(CONVERT_COUNT);
    }
}
