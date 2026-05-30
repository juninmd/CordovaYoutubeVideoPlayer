package com.bunkerpalace.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.youtube.player.YouTubeIntents;
import com.keyes.youtube.OpenYouTubePlayerActivity;
import android.os.Build;
import android.util.Log;

public class YoutubeVideoPlayer extends CordovaPlugin {

	private static final int REQUEST_CODE = 242;

	private CallbackContext callbackContext;
	private CordovaPreferences preferences;

	// Cached values for performance
	private boolean canResolvePlayVideoIntent = false;
	private boolean canResolvePlayVideoIntentWithOptions = false;
	private boolean isLollipopOrNewer = false;
	private String youTubeVersion = null;
	private boolean intentResolutionInitialized = false;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		this.preferences = cordova.getActivity().getPreferences();
		// Initialize device capability checks
		initializeDeviceCapabilities();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == this.cordova.getActivity().RESULT_OK) {
				this.callbackContext.success();
			} else {
				this.callbackContext.error("Error");
			}
		}
	}

	private void openVideo(String videoId) {
		Intent intent = createYoutubeIntent(videoId);
		cordova.startActivityForResult(this, intent, REQUEST_CODE);
	}

	private Intent createYoutubeIntent(String videoId) {
		if (isLollipopOrNewer) {
			return createLollipopYouTubeIntent(videoId);
		}
		return createLegacyYouTubeIntent(videoId);
	}

	private Intent createLollipopYouTubeIntent(String videoId) {
		Context ctx = cordova.getActivity();

		// Handle problematic YouTube version (11.16) that requires special handling
		if (hasProblematicYouTubeVersion() && canResolvePlayVideoIntent) {
			return YouTubeIntents.createPlayVideoIntent(ctx, videoId);
		}

		if (canResolvePlayVideoIntentWithOptions) {
			return YouTubeIntents.createPlayVideoIntentWithOptions(ctx, videoId, true, true);
		}

		return createCustomYouTubeIntent(videoId);
	}

	private boolean hasProblematicYouTubeVersion() {
		return getYouTubeVersion() != null && getYouTubeVersion().startsWith("11.16");
	}

	private String getYouTubeVersion() {
		if (youTubeVersion == null) {
			youTubeVersion = YouTubeIntents.getInstalledYouTubeVersionName(cordova.getActivity());
		}
		return youTubeVersion;
	}

	private void initializeDeviceCapabilities() {
		if (intentResolutionInitialized) {
			return;
		}
		
		Context ctx = cordova.getActivity();
		isLollipopOrNewer = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
		canResolvePlayVideoIntent = YouTubeIntents.canResolvePlayVideoIntent(ctx);
		canResolvePlayVideoIntentWithOptions = YouTubeIntents.canResolvePlayVideoIntentWithOptions(ctx);
		intentResolutionInitialized = true;
	}

	private Intent createCustomYouTubeIntent(String videoId) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.youtube.com/watch?v=" + videoId),
				cordova.getActivity(),
				YouTubeActivity.class);
		intent.putExtra("videoId", videoId);
		intent.putExtra("YouTubeApiId", preferences.getString("YouTubeDataApiKey", "YOUTUBE_API_KEY"));
		return intent;
	}

	private Intent createLegacyYouTubeIntent(String videoId) {
		return new Intent(null, Uri.parse("ytv://" + videoId), cordova.getActivity(), OpenYouTubePlayerActivity.class);
	}
}
