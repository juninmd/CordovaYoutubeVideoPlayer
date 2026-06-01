const app = {
    initialize: () => {
        app.bindEvents();
    },
    bindEvents: () => {
        document.addEventListener('deviceready', app.onDeviceReady, false);
    },
    onDeviceReady: () => {},
    playVideo: () => {
        YoutubeVideoPlayer.openVideo('npjF032TDDQ').then((result) => {
            console.log(`YoutubeVideoPlayer result = ${result}`);
        });
    },
};
// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', app.initialize);
export {};
//# sourceMappingURL=index.js.map
