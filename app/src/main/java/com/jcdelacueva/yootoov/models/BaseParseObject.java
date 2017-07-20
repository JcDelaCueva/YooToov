package com.jcdelacueva.yootoov.models;

import android.text.TextUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import java.util.List;

public class BaseParseObject extends ParseObject {
    public final static String CREATED_AT = "createdAt";
    public final static String UPDATED_AT = "updatedAt";

    @Override
    public <T>List<T> getList(String key){
        List<T> list = super.getList(key);

        if(list != null){
            for (int i = 0; i < list.size() ;){
                if(list.get(i) == null) {
                    list.remove(i);
                } else {
                    i++;
                }
            }
        }
        return list;
    }

    @Override
    public String getString(String key) {
        String value = "";
        try {
            value = super.getString(key);
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
        } catch (IllegalStateException ise) {

        }
        return value;
    }

    protected String getParseFileUrl(String key) {
        String url = "";
        try {
            ParseFile parseFile = getParseFile(key);
            if (parseFile != null) {
                url = parseFile.getUrl();
            }
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        }
        return url;
    }

    @Override
    public Number getNumber(String key) {
        Number value = null;
        try {
            value = super.getNumber(key);
        } catch (IllegalStateException ise) {

        }
        if (value != null) {
            return value;
        } else {
            return 0;
        }
    }
}
