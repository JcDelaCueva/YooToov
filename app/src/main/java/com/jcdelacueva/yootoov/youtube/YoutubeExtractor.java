package com.jcdelacueva.yootoov.youtube;

import android.text.TextUtils;
import com.jcdelacueva.yootoov.Constants;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeExtractor {
    private static String API_KEY = Constants.API_KEY;

    public static YoutubeVideoInfo getVideoInfo(String videoId) throws IOException, JSONException {
        YoutubeVideoInfo youtubeInfo = null;
        String stringResponse = "";

        HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host("www.googleapis.com")
            .addPathSegment("youtube")
            .addPathSegment("v3")
            .addPathSegment("videos")
            .addQueryParameter("id", videoId)
            .addQueryParameter("key", API_KEY)
            .addQueryParameter("part", "snippet,contentDetails")
            .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        stringResponse = response.body().string();

        if (!TextUtils.isEmpty(stringResponse)) {
            JSONObject jsonResponse = new JSONObject(stringResponse);
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            JSONObject jsonVidInfo = jsonArray.getJSONObject(0);
            JSONObject jsonSnippet = jsonVidInfo.getJSONObject("snippet");
            JSONObject jsonContentDetails = jsonVidInfo.getJSONObject("contentDetails");

            String id = videoId;
            String title = jsonSnippet.getString("title");
            String description = jsonSnippet.getString("description");
            String duration = jsonContentDetails.getString("duration");

            youtubeInfo = new YoutubeVideoInfo(id, title, description, duration);
        }

        return youtubeInfo;
    }
}
