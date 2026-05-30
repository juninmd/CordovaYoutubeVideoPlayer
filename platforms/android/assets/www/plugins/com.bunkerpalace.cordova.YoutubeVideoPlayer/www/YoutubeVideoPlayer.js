cordova.define("com.bunkerpalace.cordova.YoutubeVideoPlayer.YoutubeVideoPlayer", function(require, exports, module) { var exec = require('cordova/exec');

function YoutubeVideoPlayer() {}

YoutubeVideoPlayer.prototype.openVideo = function(YTid, successCallback, errorCallback) {
	exec(successCallback || function(result) { console.log(result); },
	     errorCallback || function(error) { console.log(error); },
	     "YoutubeVideoPlayer",
	     "openVideo",
	     [YTid]
	);
}

var YoutubeVideoPlayer = new YoutubeVideoPlayer();
module.exports = YoutubeVideoPlayer
});
