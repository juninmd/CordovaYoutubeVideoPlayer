package com.bunkerpalace.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.PluginResult;
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

	private CallbackContext callbackContext;

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		if(action.equals("openVideo")) {
			String url = args.getString(0);
        	this.openVideo(url);
			this.callbackContext = callbackContext;
        	return true;
        }
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 242) {
			if (resultCode == this.cordova.getActivity().RESULT_OK) {
				this.callbackContext.success();
			} else {
				this.callbackContext.error("Error");
			}
		}
	}

	private void openVideo(String videoId) {
		Intent intent = createYoutubeIntent(videoId);
		cordova.startActivityForResult(this, intent, 242);
	}

	private Intent createYoutubeIntent(String videoId) {
		// For devices running Lollipop or newer, use YouTube Intents
		if (isLollipopOrNewer()) {
			return createLollipopYouTubeIntent(videoId);
		}
		
		// For older devices, use the legacy YouTube player
		return createLegacyYouTubeIntent(videoId);
	}

	/**
	 * Creates a YouTube intent for devices running Lollipop or newer
	 */
	private Intent createLollipopYouTubeIntent(String videoId) {
		Context cordovaContext = cordova.getActivity();
		String version = YouTubeIntents.getInstalledYouTubeVersionName(cordovaContext);
		
		// Check for specific YouTube app version that needs special handling
		if (hasProblematicYouTubeVersion(version) && YouTubeIntents.canResolvePlayVideoIntent(cordovaContext)) {
			return YouTubeIntents.createPlayVideoIntent(cordovaContext, videoId);
		}
		
		// Try to use YouTube intents with options
		if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(cordovaContext)) {
			return YouTubeIntents.createPlayVideoIntentWithOptions(cordovaContext, videoId, true, true);
		}
		
		// Fall back to custom YouTube activity with API key
		return createCustomYouTubeIntent(videoId);
	}

	/**
	 * Checks if the YouTube app version is known to have issues
	 */
	private boolean hasProblematicYouTubeVersion(String version) {
		return version != null && version.startsWith("11.16");
	}

	/**
	 * Creates a custom YouTube intent using our own YouTubeActivity
	 */
	private Intent createCustomYouTubeIntent(String videoId) {
		Intent intent = new Intent(Intent.ACTION_VIEW, 
				Uri.parse("http://www.youtube.com/watch?v=" + videoId), 
				cordova.getActivity(), 
				YouTubeActivity.class);
		intent.putExtra("videoId", videoId);
		
		ConfigXmlParser parser = new ConfigXmlParser();
		parser.parse(cordova.getActivity());
		CordovaPreferences prefs = parser.getPreferences();
		intent.putExtra("YouTubeApiId", prefs.getString("YouTubeDataApiKey", "YOUTUBE_API_KEY"));
		
		return intent;
	}

	/**
	 * Creates a YouTube intent for older devices (pre-Lollipop)
	 */
	private Intent createLegacyYouTubeIntent(String videoId) {
		return new Intent(null, Uri.parse("ytv://" + videoId), cordova.getActivity(), OpenYouTubePlayerActivity.class);
	}

	/**
	 * Checks if the device is running Lollipop or newer
	 */
	private boolean isLollipopOrNewer() {
		return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
	}
}
