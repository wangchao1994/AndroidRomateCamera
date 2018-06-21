package com.android.librarycamera.materialcamera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.android.librarycamera.materialcamera.internal.BaseCaptureActivity;
import com.android.librarycamera.materialcamera.internal.Camera2Fragment;

public class CaptureActivity2 extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return Camera2Fragment.newInstance();
  }
}
