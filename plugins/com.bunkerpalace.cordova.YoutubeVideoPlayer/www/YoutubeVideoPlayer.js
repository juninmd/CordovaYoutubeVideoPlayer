var exec = require('cordova/exec');

function YoutubeVideoPlayer() {}

YoutubeVideoPlayer.prototype.openVideo = function (YTid, callback) {
    exec(
        function (result) {
            console.log(result);
            if (typeof callback === 'function') {
                callback('closed');
            }
        },
        function (error) {
            console.log(error);
            if (typeof callback === 'function') {
                callback('error');
            }
        },
        'YoutubeVideoPlayer',
        'openVideo',
        [YTid]
    );
};

module.exports = new YoutubeVideoPlayer();
