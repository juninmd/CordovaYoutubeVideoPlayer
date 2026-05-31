import exec from 'cordova/exec';
export default {
    openVideo: (YTid) => {
        return new Promise((resolve, reject) => {
            exec(() => resolve('closed'), () => reject('error'), 'YoutubeVideoPlayer', 'openVideo', [YTid]);
        });
    },
};
//# sourceMappingURL=YoutubeVideoPlayer.js.map