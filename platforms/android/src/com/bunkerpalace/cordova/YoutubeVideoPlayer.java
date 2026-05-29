package com.bunkerpalace.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
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

	private static final int PLAY_VIDEO_REQUEST_CODE = 242;
	private static final String YOUTUBE_VERSION_WITH_BUG = "11.16";

	private CallbackContext callbackContext;

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("openVideo")) {
			String url = args.getString(0);
        	this.openVideo(url);
			this.callbackContext = callbackContext;
        	return true;
        }
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == PLAY_VIDEO_REQUEST_CODE) {
			if (resultCode == this.cordova.getActivity().RESULT_OK) {
				this.callbackContext.success();
			} else {
				this.callbackContext.error("Error");
			}
		}
	}

	private void openVideo(String videoId) {
		Intent intent = createYoutubeIntent(videoId);
		cordova.startActivityForResult(this, intent, PLAY_VIDEO_REQUEST_CODE);
	}

	private Intent createYoutubeIntent(String videoId) {
		if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
			return new Intent(null, Uri.parse("ytv://" + videoId), cordova.getActivity(), OpenYouTubePlayerActivity.class);
		}
		return createLollipopYoutubeIntent(videoId);
	}

	private Intent createLollipopYoutubeIntent(String videoId) {
		Context context = cordova.getActivity();
		String version = YouTubeIntents.getInstalledYouTubeVersionName(context);

		if (version != null && version.startsWith(YOUTUBE_VERSION_WITH_BUG) && YouTubeIntents.canResolvePlayVideoIntent(context)) {
			return YouTubeIntents.createPlayVideoIntent(context, videoId);
		}
		if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(context)) {
			return YouTubeIntents.createPlayVideoIntentWithOptions(context, videoId, true, true);
		}
		return createFallbackIntent(videoId, context);
	}

	private Intent createFallbackIntent(String videoId, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId), context, YouTubeActivity.class);
		intent.putExtra("videoId", videoId);
		ConfigXmlParser parser = new ConfigXmlParser();
		parser.parse(context);
		CordovaPreferences prefs = parser.getPreferences();
		intent.putExtra("YouTubeApiId", prefs.getString("YouTubeDataApiKey", "YOUTUBE_API_KEY"));
		return intent;
	}
}
