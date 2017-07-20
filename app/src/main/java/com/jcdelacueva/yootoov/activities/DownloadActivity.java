package com.jcdelacueva.yootoov.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jcdelacueva.yootoov.R;
import com.jcdelacueva.yootoov.tasks.DownloadMp3Task;
import com.jcdelacueva.yootoov.youtube.YouTubeThumbnail;
import com.jcdelacueva.yootoov.youtube.YoutubeVideoInfo;

public class DownloadActivity extends BaseActivity implements DownloadMp3Task.VideoInfoCallback {

    @BindView(R.id.imgPreview) ImageView imgPreview;
    @BindView(R.id.imgProgress) ImageView imgProgress;
    @BindView(R.id.tvPercentage) TextView tvPercentage;
    @BindView(R.id.etLink) EditText etLink;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvLength) TextView tvLength;
    @BindView(R.id.llInputs) LinearLayout llInputs;
    @BindView(R.id.llProcessing) LinearLayout llProcessing;
    @BindView(R.id.flProgress) FrameLayout flProgress;

    private boolean isDownloadInProgress = false;
    private String mUrl;
    private long mProgress = 0;
    private boolean isFromIntent;

    private Handler mProgresshandler = new Handler();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Intent.ACTION_SEND)) {
                    isFromIntent = true;
                    String type = intent.getType();
                    if (type.equals("text/plain")) {
                        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                        downloadMp3(sharedText);
                    }
                }
            }
        }
    }

    @Override public void onGet(YoutubeVideoInfo video) {
        if (video != null) {
            String title = video.getTitle();
            String length = video.getDuration();

            tvTitle.setText(title);
            tvLength.setText(length);

            llProcessing.setVisibility(View.GONE);
            flProgress.setVisibility(View.VISIBLE);

            tvPercentage.setText("0%");

            loadImage(YouTubeThumbnail.getUrlFromVideoId(video.getId(), YouTubeThumbnail.Quality.MAXIMUM), imgPreview);

            startDummyProgress();
        }
    }

    @OnClick(R.id.mrBack) public void onBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.mrDownload) public void onDownload() {
        etLink.setError(null);
        String videoUrl = etLink.getText().toString();
        if (DownloadMp3Task.validateUrl(videoUrl)) {
            downloadMp3(videoUrl);
        } else {
            etLink.setError("Invalid youtube url");
        }
    }

    private void downloadMp3(String url) {
        llInputs.setVisibility(View.GONE);
        llProcessing.setVisibility(View.VISIBLE);

        isDownloadInProgress = true;

        mUrl = url;

        DownloadMp3Task dlTask = new DownloadMp3Task(url);
        dlTask.setVideoDetailsCallBack(this);
        dlTask.startDownload(new DownloadMp3Task.DownloadCallback() {
            @Override public void onDone(String path, Exception e) {
                isDownloadInProgress = false;
                if (e == null) {
                    finishDownloadProgress();
                } else {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (isFromIntent) {
                        setResult(RESULT_NEGATIVE);
                        finish();
                    }
                }
            }
        });
    }

    public static Intent create(Context context) {
        Intent intent = new Intent(context, DownloadActivity.class);
        return intent;
    }

    public static void start(Context context) {
        context.startActivity(create(context));
    }

    private void finishDownloadProgress() {
        mProgresshandler.removeCallbacksAndMessages(null);
        mProgresshandler.postDelayed(new Runnable() {
            @Override public void run() {
                mProgress = mProgress + 1;
                ClipDrawable cd = (ClipDrawable) imgProgress.getBackground();
                cd.setLevel((int) (10000 - (mProgress * 100)));

                if (mProgress < 100) {
                    tvPercentage.setText(mProgress + "%");
                } else {
                    tvPercentage.setText("Done!");
                    finishDownload();
                    return;
                }

                mProgresshandler.postDelayed(this, 10);
            }
        }, 10);
    }

    private void startDummyProgress() {
        mProgresshandler.postDelayed(new Runnable() {
            @Override public void run() {
                mProgress = mProgress + 1;
                ClipDrawable cd = (ClipDrawable) imgProgress.getBackground();
                cd.setLevel((int) (10000 - (mProgress * 100)));
                tvPercentage.setText(mProgress + "%");

                int delay = 0;
                if (mProgress >= 0 && mProgress < 60) {
                    delay = 100;
                } else if (mProgress >= 60 && mProgress < 70) {
                    delay = 200;
                } else if (mProgress >= 70 && mProgress < 80) {
                    delay = 500;
                } else if (mProgress >= 80 && mProgress < 90) {
                    delay = 200;
                } else if (mProgress >= 90 && mProgress < 95) {
                    delay = 10000;
                } else {
                    return;
                }

                mProgresshandler.postDelayed(this, delay);
            }
        }, 100);
    }

    private void finishDownload() {
        isDownloadInProgress = false;
        Toast.makeText(mContext, "Download Finished", Toast.LENGTH_LONG).show();
        if (isFromIntent) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
