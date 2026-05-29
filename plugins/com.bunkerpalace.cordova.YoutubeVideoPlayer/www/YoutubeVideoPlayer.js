var exec = require('cordova/exec');

function YoutubeVideoPlayer() {}

YoutubeVideoPlayer.prototype.openVideo = function(videoId, callback) {
	exec(
		function() {
			if (callback) {
				callback('closed');
			}
		},
		function() {
			if (callback) {
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