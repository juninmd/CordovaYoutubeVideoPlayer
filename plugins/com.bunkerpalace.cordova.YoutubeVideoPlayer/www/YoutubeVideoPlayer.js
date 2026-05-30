var exec = require('cordova/exec');

module.exports = {
    openVideo: function (YTid, callback) {
        var cb = typeof callback === 'function' ? callback : null;
        exec(
            function () {
                cb && cb('closed');
            },
            function () {
                cb && cb('error');
            },
            'YoutubeVideoPlayer',
            'openVideo',
            [YTid]
        );
    },
};
