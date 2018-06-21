package com.android.librarycamera.materialcamera;

import android.app.Fragment;
import android.support.annotation.NonNull;
import com.android.librarycamera.materialcamera.internal.BaseCaptureActivity;
import com.android.librarycamera.materialcamera.internal.CameraFragment;

public class CaptureActivity extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return CameraFragment.newInstance();
  }
}
