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
	private Context cachedContext;
	private String cachedApiKey;

	private Boolean canResolvePlayVideoIntent;
	private Boolean canResolvePlayVideoIntentWithOptions;
	private Boolean isLollipopOrNewer;
	private String youTubeVersion;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		this.cachedContext = cordova.getActivity();
		CordovaPreferences preferences = cordova.getActivity().getPreferences();
		this.cachedApiKey = preferences.getString("YouTubeDataApiKey", "YOUTUBE_API_KEY");
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
		this.cordova.startActivityForResult(this, intent, REQUEST_CODE);
	}

	private Intent createYoutubeIntent(String videoId) {
		if (isLollipopOrNewer()) {
			return createLollipopYouTubeIntent(videoId);
		}
		return createLegacyYouTubeIntent(videoId);
	}

	private Intent createLollipopYouTubeIntent(String videoId) {
		Context ctx = this.cachedContext;

		if (hasProblematicYouTubeVersion() && canResolvePlayVideoIntent(ctx)) {
			return YouTubeIntents.createPlayVideoIntent(ctx, videoId);
		}

		if (canResolvePlayVideoIntentWithOptions(ctx)) {
			return YouTubeIntents.createPlayVideoIntentWithOptions(ctx, videoId, true, true);
		}

		return createCustomYouTubeIntent(videoId);
	}

	private boolean hasProblematicYouTubeVersion() {
		String version = getYouTubeVersion();
		return version != null && version.startsWith("11.16");
	}

	private String getYouTubeVersion() {
		if (youTubeVersion == null) {
   youTubeVersion = YouTubeIntents.getInstalledYouTubeVersionName(cordova.getActivity());
		}
		return youTubeVersion;
	}

	private boolean canResolvePlayVideoIntent(Context ctx) {
		if (canResolvePlayVideoIntent == null) {
			canResolvePlayVideoIntent = YouTubeIntents.canResolvePlayVideoIntent(ctx);
		}
		return canResolvePlayVideoIntent;
	}

	private boolean canResolvePlayVideoIntentWithOptions(Context ctx) {
		if (canResolvePlayVideoIntentWithOptions == null) {
			canResolvePlayVideoIntentWithOptions = YouTubeIntents.canResolvePlayVideoIntentWithOptions(ctx);
		}
		return canResolvePlayVideoIntentWithOptions;
	}

	private Intent createCustomYouTubeIntent(String videoId) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.youtube.com/watch?v=" + videoId),
				this.cachedContext,
				YouTubeActivity.class);
		intent.putExtra("videoId", videoId);
		intent.putExtra("YouTubeApiId", this.cachedApiKey);
		return intent;
	}

	private Intent createLegacyYouTubeIntent(String videoId) {
		return new Intent(null, Uri.parse("ytv://" + videoId), this.cachedContext, OpenYouTubePlayerActivity.class);
	}

	private boolean isLollipopOrNewer() {
		if (isLollipopOrNewer == null) {
			isLollipopOrNewer = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
		}
		return isLollipopOrNewer;
	}
}
