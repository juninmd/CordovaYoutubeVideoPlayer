package com.bunkerpalace.cordova;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;

public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        PlayerStateChangeListener {

    private static final int RECOVERY_REQUEST = 1;

    private YouTubePlayerView youTubeView;
    private String videoId;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        videoId = intent != null ? intent.getStringExtra("videoId") : null;
        apiKey = intent != null ? intent.getStringExtra("YouTubeApiId") : null;

        if (videoId == null || videoId.isEmpty() || apiKey == null || apiKey.isEmpty()) {
            Toast.makeText(this, "Invalid video ID or API key", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        youTubeView = new YouTubePlayerView(this);
        youTubeView.initialize(apiKey, this);
        setContentView(youTubeView);
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo(videoId);
            player.setPlayerStateChangeListener(this);
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        handleInitializationError(errorReason);
    }

    @Override
    public void onVideoEnded() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Log.d("YouTubeActivity", "onError(): " + errorReason.toString());
        finish();
    }

    @Override
    public void onAdStarted() {}

    @Override
    public void onLoaded(String videoId) {}

    @Override
    public void onLoading() {}

    @Override
    public void onVideoStarted() {}

    private void handleInitializationError(YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Error initializing YouTube player: %s", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}
