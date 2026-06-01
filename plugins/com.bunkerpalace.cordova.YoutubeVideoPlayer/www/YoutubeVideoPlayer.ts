import exec from 'cordova/exec';

interface YoutubeVideoPlayer {
  openVideo: (YTid: string) => Promise<string>;
}

export default {
  openVideo: (YTid: string): Promise<string> => {
    return new Promise((resolve, reject) => {
      exec(
        () => resolve('closed'),
        () => reject('error'),
        'YoutubeVideoPlayer',
        'openVideo',
        [YTid]
      );
    });
  },
} as YoutubeVideoPlayer;