package com.example.android.bakingapp.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class VideoPlayer {

    private static final String TAG = VideoPlayer.class.getSimpleName();
    private final Context context;
    private final String url;

    public VideoPlayer(Context context, String url, PlayerView playerView, ProgressBar progressBar) {

        this.context = context;
        this.url = url;
        this.playerView = playerView;
        this.progressBar = progressBar;
    }

    private PlayerView playerView;
    private ProgressBar progressBar;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playBackPosition = 0;
    private SimpleExoPlayer simpleExoPlayer;

    public void initializePlayer() {
        simpleExoPlayer = ExoPlayerFactory
                .newSimpleInstance(
                        new DefaultRenderersFactory(context),
                        new DefaultTrackSelector(),
                        new DefaultLoadControl());

        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
        simpleExoPlayer.seekTo(currentWindow, playBackPosition);

        Log.e(TAG, "Uri Passed to VideoPlayer oooh --- -" + url);
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        simpleExoPlayer.prepare(mediaSource, true, false);
    }

    //TODO: Use the listener to create progress bar theres netwrk issue etc
    private MediaSource buildMediaSource(Uri uri) {
        Log.i(TAG, "current url " + uri);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                if (playbackState == ExoPlayer.STATE_BUFFERING){
//                    progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
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
        });
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("baking app"))
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    public void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void releasePlayer() {
        if (simpleExoPlayer != null) {
            playBackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
            playerView.setPlayer(null);
            simpleExoPlayer = null;
        }
    }

    public boolean isPlayWhenReady() {
        return playWhenReady;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }

    public int getCurrentWindow() {
        return currentWindow;
    }

    public void setCurrentWindow(int currentWindow) {
        this.currentWindow = currentWindow;
    }

    public long getPlayBackPosition() {
        return playBackPosition;
    }

    public void setPlayBackPosition(long playBackPosition) {
        this.playBackPosition = playBackPosition;
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return simpleExoPlayer;
    }
}
