declare const YoutubeVideoPlayer: {
    openVideo: (videoId: string, callback: (result: string) => void) => void;
};

const app = {
    initialize: () => {
        app.bindEvents();
    },
    bindEvents: () => {
        document.addEventListener('deviceready', app.onDeviceReady, false);
    },
    onDeviceReady: () => {},
    playVideo: () => {
        YoutubeVideoPlayer.openVideo('npjF032TDDQ', (result: string) => {
            console.log(`YoutubeVideoPlayer result = ${result}`);
        });
    },
};

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', app.initialize);
