package com.pavelprymak.bakingapp.utils.player.mediaSession.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.pavelprymak.bakingapp.MainActivity;
import com.pavelprymak.bakingapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.pavelprymak.bakingapp.App.CHANNEL_ID;

public class MediaSessionNotificationsManager {
    private Context mContext;
    private NotificationManager mNotificationManager;

    public MediaSessionNotificationsManager(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotification(PlaybackStateCompat state, MediaSessionCompat.Token mediaSessionToken, String recipeDescription) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = mContext.getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = mContext.getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new NotificationCompat
                .Action(R.drawable.exo_controls_previous, mContext.getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (mContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (mContext, 0, new Intent(mContext, MainActivity.class), 0);

        builder.setContentTitle(mContext.getString(R.string.app_name))
                .setContentText(recipeDescription)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionToken)
                        .setShowActionsInCompactView(0, 1));


        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public void release() {
        if (mNotificationManager != null)
            mNotificationManager.cancelAll();
    }
}
