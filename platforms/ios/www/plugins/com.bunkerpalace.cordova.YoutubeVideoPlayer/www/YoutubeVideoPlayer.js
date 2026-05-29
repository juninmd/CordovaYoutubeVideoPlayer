cordova.define("com.bunkerpalace.cordova.YoutubeVideoPlayer.YoutubeVideoPlayer", function(require, exports, module) { var exec = require('cordova/exec');

function YoutubeVideoPlayer() {}

YoutubeVideoPlayer.prototype.openVideo = function(videoId, callback) {
	exec(
		function() {
			if (typeof callback === 'function') {
				callback('closed');
			}
		},
		function() {
			if (typeof callback === 'function') {
				callback('error');
			}
		},
		"YoutubeVideoPlayer",
		"openVideo",
		[videoId]
	);
};

var youtubeVideoPlayer = new YoutubeVideoPlayer();
module.exports = youtubeVideoPlayer;
});
