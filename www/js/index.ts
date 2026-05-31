import type { VideoResult } from '../../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer';

declare const YoutubeVideoPlayer: {
  openVideo: (videoId: string) => Promise<VideoResult>;
};

const app = {
  initialize: (): void => {
    app.bindEvents();
  },
  bindEvents: (): void => {
    document.addEventListener('deviceready', app.onDeviceReady, false);
  },
  onDeviceReady: (): void => {},
  async playVideo(): Promise<void> {
    try {
      const result = await YoutubeVideoPlayer.openVideo('npjF032TDDQ');
      console.log(`YoutubeVideoPlayer result = ${result}`);
    } catch (error) {
      console.error('YoutubeVideoPlayer error:', error);
    }
  },
};

document.addEventListener('DOMContentLoaded', app.initialize);
