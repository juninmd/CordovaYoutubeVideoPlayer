const app = {
    initialize() {
        this.bindEvents();
    },
    bindEvents() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    onDeviceReady() {
    },
    playVideo() {
        YoutubeVideoPlayer.openVideo('npjF032TDDQ', result => console.log('YoutubeVideoPlayer result = ' + result));
    },
};
