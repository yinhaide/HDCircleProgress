package com.yhd.circleprogress.app.frag;

import android.view.View;
import android.widget.TextView;

import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;
import com.yhd.circleprogress.CircleProgressView;
import com.yhd.circleprogress.app.R;

/**
 * 双波浪曲线
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/6 16:12.
 */
public class Frag_circleprogress extends RoFragment {
    @BindView(R.id.cpb)
    private CircleProgressView cpBar;
    @BindView(R.id.tv_progress)
    private TextView tvProgress;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_circleprogress;
    }

    @Override
    public void initViewFinish(View inflateView) {

    }

    @Override
    public void onNexts(Object object) {

    }

    @Event(R.id.bt_thirty)
    private void thirty(View view) {
        cpBar.setProgress(0.0f);
        tvProgress.setText("0%");
    }

    @Event(R.id.bt_sixty)
    private void sixty(View view) {
        cpBar.setProgress(0.5f);
        tvProgress.setText("50%");
    }

    @Event(R.id.bt_nity)
    private void nity(View view) {
        cpBar.setProgress(1.0f);
        tvProgress.setText("100%");
    }
}
