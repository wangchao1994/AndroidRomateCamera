package com.android.librarycamera.materialcamera.internal;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.librarycamera.R;

/** @author Aidan Follestad (afollestad) */
public class PlaybackVideoFragment extends Fragment
    implements CameraUriInterface {

  private String mOutputUri;
  private BaseCaptureInterface mInterface;

  private Handler mCountdownHandler;


  @SuppressWarnings("deprecation")
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mInterface = (BaseCaptureInterface) activity;
  }

  public static PlaybackVideoFragment newInstance(
      String outputUri, boolean allowRetry, int primaryColor) {
    PlaybackVideoFragment fragment = new PlaybackVideoFragment();
    fragment.setRetainInstance(true);
    Bundle args = new Bundle();
    args.putString("output_uri", outputUri);
    args.putBoolean(CameraIntentKey.ALLOW_RETRY, allowRetry);
    args.putInt(CameraIntentKey.PRIMARY_COLOR, primaryColor);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (getActivity() != null)
      getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //
    return null;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


  }

  private void startCountdownTimer() {

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  private void useVideo() {

  }

  @Override
  public String getOutputUri() {
    return getArguments().getString("output_uri");
  }


}
