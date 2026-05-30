package com.bunkerpalace.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.youtube.player.YouTubeIntents;
import com.keyes.youtube.OpenYouTubePlayerActivity;
import android.os.Build;

public class YoutubeVideoPlayer extends CordovaPlugin {

    private static final int OPEN_VIDEO_REQUEST_CODE = 242;

    private CallbackContext callbackContext;
    private CordovaPreferences preferences;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.preferences = cordova.getActivity().getPreferences();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("openVideo")) {
            String videoId = args.getString(0);
            this.openVideo(videoId);
            this.callbackContext = callbackContext;
            return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == OPEN_VIDEO_REQUEST_CODE) {
            if (resultCode == this.cordova.getActivity().RESULT_OK) {
                this.callbackContext.success();
            } else {
                this.callbackContext.error("Error");
            }
        }
    }

    private void openVideo(String videoId) {
        Intent intent = createYoutubeIntent(videoId);
        cordova.startActivityForResult(this, intent, OPEN_VIDEO_REQUEST_CODE);
    }

    private Intent createYoutubeIntent(String videoId) {
        if (isLollipopOrNewer()) {
            return createLollipopYouTubeIntent(videoId);
        }
        return createLegacyYouTubeIntent(videoId);
    }

    private Intent createLollipopYouTubeIntent(String videoId) {
        Context context = cordova.getActivity();
        String version = YouTubeIntents.getInstalledYouTubeVersionName(context);

        if (hasProblematicYouTubeVersion(version)
            && YouTubeIntents.canResolvePlayVideoIntent(context)) {
            return YouTubeIntents.createPlayVideoIntent(context, videoId);
        }

        if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(context)) {
            return YouTubeIntents.createPlayVideoIntentWithOptions(
                context, videoId, true, true
            );
        }

        return createCustomYouTubeIntent(videoId);
    }

    private boolean hasProblematicYouTubeVersion(String version) {
        return version != null && version.startsWith("11.16");
    }

    private Intent createCustomYouTubeIntent(String videoId) {
        Intent intent = new Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=" + videoId),
            cordova.getActivity(),
            YouTubeActivity.class
        );
        intent.putExtra("videoId", videoId);
        intent.putExtra(
            "YouTubeApiId",
            preferences.getString("YouTubeDataApiKey", "YOUTUBE_API_KEY")
        );
        return intent;
    }

    private Intent createLegacyYouTubeIntent(String videoId) {
        return new Intent(
            null,
            Uri.parse("ytv://" + videoId),
            cordova.getActivity(),
            OpenYouTubePlayerActivity.class
        );
    }

    private boolean isLollipopOrNewer() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    }
}
