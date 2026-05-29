"use strict";
const exec = require("cordova/exec");
class YoutubeVideoPlayer {
    openVideo(YTid, callback) {
        return new Promise((resolve, reject) => {
            exec((result) => {
                console.log(result);
                callback?.('closed');
                resolve('closed');
            }, (error) => {
                console.error(error);
                callback?.('error');
                reject(new Error(error ?? 'Unknown error opening video'));
            }, 'YoutubeVideoPlayer', 'openVideo', [YTid]);
        });
    }
}
const youtubeVideoPlayer = new YoutubeVideoPlayer();
module.exports = youtubeVideoPlayer;
//# sourceMappingURL=YoutubeVideoPlayer.js.map
