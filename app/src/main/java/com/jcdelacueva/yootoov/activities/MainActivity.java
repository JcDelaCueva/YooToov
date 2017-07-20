package com.jcdelacueva.yootoov.activities;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jcdelacueva.yootoov.R;

public class MainActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnStart) public void onClick() {
        DownloadActivity.start(mActivity);
    }
}
