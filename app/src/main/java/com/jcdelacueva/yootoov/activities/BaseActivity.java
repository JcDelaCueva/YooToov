package com.jcdelacueva.yootoov.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.jcdelacueva.yootoov.R;
import com.squareup.picasso.Picasso;

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected BaseActivity mActivity;

    public static final int REQ_PERMISSION = 222;
    public static final int RESULT_NEGATIVE = -123;

    private ProgressBar progress;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mActivity = BaseActivity.this;
    }

    @Override public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        progress = (ProgressBar) findViewById(R.id.progress);
    }

    @Override protected void attachBaseContext(Context newBase) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        cancelQueries();
    }

    protected void loadImage(String url, ImageView imageView) {
        Picasso.with(mActivity).load(url).into(imageView);
    }

    protected void cancelQueries() {

    }

    protected boolean isActivityAlive() {
        return !(isFinishing() || isDestroyed() || BaseActivity.this == null);
    }

    public void showProgress() {
        if (progress == null) {

        }

        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (progress == null) {

        }

        progress.setVisibility(View.GONE);
    }
}
