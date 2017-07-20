package com.jcdelacueva.yootoov.tasks;

import android.text.TextUtils;
import bolts.Continuation;
import bolts.Task;
import com.jcdelacueva.yootoov.Constants;
import com.jcdelacueva.yootoov.YootoovApp;
import com.jcdelacueva.yootoov.models.Convert;
import com.jcdelacueva.yootoov.youtube.YouTubeUrlParser;
import com.jcdelacueva.yootoov.youtube.YoutubeExtractor;
import com.jcdelacueva.yootoov.youtube.YoutubeVideoInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import static com.jcdelacueva.yootoov.models.Convert.STATUS_DOWNLOADED;
import static com.jcdelacueva.yootoov.models.Convert.STATUS_PENDING;

public class DownloadMp3Task {
    private String mUrlString;
    private DownloadCallback mDownLoadListener;
    private VideoInfoCallback mVideoInfoCallback;
    private String mTitle;
    private Convert mConvert;

    public interface DownloadCallback {
        void onDone(String path, Exception e);
    }

    public interface VideoInfoCallback {
        void onGet(YoutubeVideoInfo video);
    }

    public DownloadMp3Task(String urlString) {
        mUrlString = urlString;
    }

    public void startDownload(DownloadCallback downloadCallback) {
        mDownLoadListener = downloadCallback;
        if (mDownLoadListener == null) {
            return;
        }

        if (!validateUrl(mUrlString)) {
            mDownLoadListener.onDone("", new RuntimeException("Invalid Youtube url"));
            return;
        }

        //All good start sequence
        downloadSequence();
    }

    private void downloadSequence() {
        Task.callInBackground(new Callable<String>() {
            @Override public String call() throws Exception {
                String stringResponse = "";
                OkHttpClient client = new OkHttpClient();

                HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(Constants.y2mp3ApiHost)
                    .addPathSegment(Constants.y2mp3ApiSegment)
                    .addQueryParameter("format", "JSON")
                    .addQueryParameter("video", mUrlString)
                    .build();
                Request request = new Request.Builder().url(url).build();

                Response response = client.newCall(request).execute();
                stringResponse = response.body().string();
                return stringResponse;
            }
        }).onSuccess(new Continuation<String, JSONObject>() {
            @Override public JSONObject then(Task<String> task) throws Exception {
                String stringResponse = task.getResult();
                JSONObject jsonResponse = new JSONObject(stringResponse);
                return jsonResponse;
            }
        }).onSuccess(new Continuation<JSONObject, String>() {
            @Override public String then(Task<JSONObject> task) throws Exception {
                final JSONObject jsonResponse = task.getResult();
                String link = jsonResponse.getString("link");
                return link;
            }
        }).onSuccess(new Continuation<String, String>() {
            @Override public String then(Task<String> task) throws Exception {
                String link = task.getResult();
                final YoutubeVideoInfo videoInfo = YoutubeExtractor.getVideoInfo(YouTubeUrlParser.getVideoId(mUrlString));
                mTitle = videoInfo.getTitle();

                mConvert = new Convert();
                mConvert.setTitle(mTitle);
                mConvert.setUrl(mUrlString);
                mConvert.setDuration(videoInfo.getDuration());
                mConvert.setStatus(STATUS_PENDING);
                mConvert.save();

                Task.UI_THREAD_EXECUTOR.execute(new Runnable() {
                    @Override public void run() {
                        if (mVideoInfoCallback != null) {
                            mVideoInfoCallback.onGet(videoInfo);
                        }
                    }
                });
                return link;
            }
        }).onSuccess(new Continuation<String, byte[]>() {
            @Override public byte[] then(Task<String> task) throws Exception {
                String link = task.getResult();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(link).build();
                Response response = client.newCall(request).execute();
                return response.body().bytes();
            }
        }).onSuccess(new Continuation<byte[], String>() {
            @Override public String then(Task<byte[]> task) throws Exception {
                byte[] in = task.getResult();

                File downloadDir = new File(YootoovApp.getDownloadPath());
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(YootoovApp.getDownloadPath() + "/" + mTitle + ".mp3");
                fos.write(in);
                fos.close();

                return YootoovApp.getDownloadPath() + "/" + mTitle + ".mp3";
            }
        }).onSuccess(new Continuation<String, String>() {
            @Override public String then(Task<String> task) throws Exception {
                mConvert.setStatus(STATUS_DOWNLOADED);
                mConvert.save();
                return task.getResult();
            }
        }).continueWith(new Continuation<String, Object>() {
            @Override public Object then(Task<String> task) throws Exception {
                final String path = task.getResult();
                final Exception exception = task.getError();
                Task.UI_THREAD_EXECUTOR.execute(new Runnable() {
                    @Override public void run() {
                        if (mDownLoadListener != null) {
                            mDownLoadListener.onDone(path, exception);
                        }
                    }
                });
                return null;
            }
        });
    }

    public void setVideoDetailsCallBack(VideoInfoCallback getCallback) {
        mVideoInfoCallback = getCallback;
    }

    public static boolean validateUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        } else {
            return (url.contains("https") || url.contains("youtu.be") || url.contains("youtube.com"));
        }
    }
}
