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

/**
 * Activity for playing YouTube videos using the YouTube Android Player API.
 * Handles video initialization, playback, and lifecycle events.
 */
public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        PlayerStateChangeListener {

    /** Request code for error recovery dialog */
    private static final int RECOVERY_REQUEST = 1;
    
    /** View for displaying YouTube video */
    private YouTubePlayerView youTubeView;
    
    /** ID of the YouTube video to play */
    private String videoId;
    
    /** API key for YouTube Data API */
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Extract video ID and API key from intent
        Intent intent = getIntent();
        videoId = intent != null ? intent.getStringExtra("videoId") : null;
        apiKey = intent != null ? intent.getStringExtra("YouTubeApiId") : null;

        if (videoId == null || videoId.isEmpty() || apiKey == null || apiKey.isEmpty()) {
            Toast.makeText(this, "Invalid video ID or API key", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // Initialize YouTube player view
        youTubeView = new YouTubePlayerView(this);
        youTubeView.initialize(apiKey, this);
        setContentView(youTubeView);
    }

    // YouTubePlayer.OnInitializedListener implementation
    
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        // Only load video if this is a new initialization (not a restoration)
        if (!wasRestored) {
            player.loadVideo(videoId);
            player.setPlayerStateChangeListener(this);
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        handleInitializationError(errorReason);
    }

    // PlayerStateChangeListener implementation
    
    @Override
    public void onVideoEnded() {
        // Video finished successfully
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Log.d("YouTubeActivity", "onError(): " + errorReason.toString());
        finish();
    }

    @Override
    public void onAdStarted() {
        // Ad started - intentionally left empty for future extension
    }

    @Override
    public void onLoaded(String videoId) {
        // Video loaded - intentionally left empty for future extension
    }

    @Override
    public void onLoading() {
        // Video loading - intentionally left empty for future extension
    }

    @Override
    public void onVideoStarted() {
        // Video playback started - intentionally left empty for future extension
    }

    /**
     * Handles YouTube player initialization errors.
     * 
     * @param errorReason The reason for initialization failure
     */
    private void handleInitializationError(YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            // Show error dialog that allows user to recover (e.g., install/update YouTube app)
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            // Show toast with error message for non-recoverable errors
            String error = String.format("Error initializing YouTube player: %s", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}
