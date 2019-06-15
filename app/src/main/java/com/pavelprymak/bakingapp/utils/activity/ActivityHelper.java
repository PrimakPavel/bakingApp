package com.pavelprymak.bakingapp.utils.activity;

import android.app.Activity;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityHelper {

    public static void setWakeLock(Activity activity, boolean isWakeLockOn) {
        if (activity != null) {
            if (isWakeLockOn) {
                // WAKE_LOCK ON
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                // WAKE_LOCK OFF
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

        }
    }

    public static void setFullScreen(Activity activity, boolean isFullScreen) {
        if (activity != null) {
            if (isFullScreen) {
                // FULL_SCREEN ON
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                // FULL_SCREEN OFF
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

        }
    }

    public static void setAppBarVisibility(Activity activity, boolean isVisible) {
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                if (isVisible) {
                    actionBar.show();
                } else {
                    actionBar.hide();
                }
            }
        }
    }
}
