package com.pavelprymak.bakingapp.utils.player;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.pavelprymak.bakingapp.utils.player.audioFocus.AudioFocusHelper;
import com.pavelprymak.bakingapp.utils.player.audioFocus.ExoAudioFocusListener;
import com.pavelprymak.bakingapp.utils.player.mediaSession.MediaSessionHelper;

public class PlayerHelper implements Player.EventListener {
    public static final int DEFAULT_RESUME_WINDOW = C.INDEX_UNSET;
    public static final long DEFAULT_RESUME_POSITION = C.TIME_UNSET;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private ProgressBar mProgressBar;
    private String mVideoDescription;
    private Context mContext;
    //AUDIO FOCUS
    private AudioFocusHelper mAudioFocusHelper;
    //MediaSessionHelper
    private MediaSessionHelper mMediaSessionHelper;


    private int resumeWindow = DEFAULT_RESUME_WINDOW;
    private long resumePosition = DEFAULT_RESUME_POSITION;

    public PlayerHelper(Context context, PlayerView mPlayerView, ProgressBar progressBar) {
        mContext = context;
        this.mPlayerView = mPlayerView;
        mProgressBar = progressBar;
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */

    public void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance((mContext), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
        }
        if (mAudioFocusHelper == null) {
            //AUDIO FOCUS PREPARE
            ExoAudioFocusListener audioFocusListener = new ExoAudioFocusListener(mExoPlayer);
            mAudioFocusHelper = new AudioFocusHelper(mContext, audioFocusListener);
        }
        if (mMediaSessionHelper == null) {
            mMediaSessionHelper = new MediaSessionHelper(mContext, new MySessionCallback());
        }
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(mContext, "BakingApp");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(buildDataSourceFactory(mContext, userAgent)).createMediaSource(mediaUri);
        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mExoPlayer.seekTo(resumeWindow, resumePosition);
        }
        if (mAudioFocusHelper.requestAudioFocus()) {
            mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
        }
        //start player
        mExoPlayer.setPlayWhenReady(true);
    }

    public void stopCurrentVideo() {
        if (mExoPlayer != null) {
            mExoPlayer.stop(true);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void setMediaDescriptionText(String mediaDescriptionsText) {
        mVideoDescription = mediaDescriptionsText;
    }

    /**
     * Release ExoPlayer.
     */
    public void releasePlayer() {
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.abandonAudioFocus();
            mAudioFocusHelper = null;
        }
        if (mExoPlayer != null) {
            updateResumePosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSessionHelper != null) {
            mMediaSessionHelper.release();
            mMediaSessionHelper = null;
        }
    }

    public void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    public void setResumePosition(int resWindow, long resPosition) {
        resumeWindow = resWindow;
        resumePosition = resPosition;
    }

    public int getCurrentResumeWindow() {
        return resumeWindow;
    }

    public long getCurrentResumePosition() {
        return resumePosition;
    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    public class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

    }

    private void updateResumePosition() {
        resumeWindow = mExoPlayer.getCurrentWindowIndex();
        resumePosition = Math.max(0, mExoPlayer.getContentPosition());
    }

    private DataSource.Factory buildDataSourceFactory(Context context, String userAgent) {
        return new DefaultDataSourceFactory(context, mTransferListener, buildHttpDataSourceFactory(userAgent));
    }

    private TransferListener mTransferListener = new TransferListener() {

        @Override
        public void onTransferInitializing(DataSource source, DataSpec dataSpec, boolean isNetwork) {

        }

        @Override
        public void onTransferStart(DataSource source, DataSpec dataSpec, boolean isNetwork) {

        }

        @Override
        public void onBytesTransferred(DataSource source, DataSpec dataSpec, boolean isNetwork, int bytesTransferred) {

        }

        @Override
        public void onTransferEnd(DataSource source, DataSpec dataSpec, boolean isNetwork) {

        }
    };

    /**
     * Returns a {@link HttpDataSource.Factory}.
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(
            String userAgent) {
        return new DefaultHttpDataSourceFactory(userAgent, mTransferListener);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (playbackState == Player.STATE_ENDED) {
            mPlayerView.hideController();
            mProgressBar.setVisibility(View.GONE);
            if (mAudioFocusHelper != null) {
                mAudioFocusHelper.abandonAudioFocus();
            }
        }

        if (mMediaSessionHelper != null
                && mMediaSessionHelper.getStateBuilder() != null
                && mMediaSessionHelper.getMediaSession() != null) {

            if ((playbackState == Player.STATE_READY) && playWhenReady) {
                mMediaSessionHelper.getStateBuilder().setState(PlaybackStateCompat.STATE_PLAYING,
                        mExoPlayer.getCurrentPosition(), 1f);
                mProgressBar.setVisibility(View.GONE);
            } else if ((playbackState == Player.STATE_READY)) {
                mMediaSessionHelper.getStateBuilder().setState(PlaybackStateCompat.STATE_PAUSED,
                        mExoPlayer.getCurrentPosition(), 1f);
                mProgressBar.setVisibility(View.GONE);
            }
            mMediaSessionHelper.getMediaSession().setPlaybackState(mMediaSessionHelper.getStateBuilder().build());
        } else if ((playbackState == Player.STATE_READY)) {
            mProgressBar.setVisibility(View.GONE);
        }

        //SHOW NOTIFICATION
        if (mMediaSessionHelper != null) {
            mMediaSessionHelper.showNotification(mVideoDescription);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
