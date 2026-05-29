import exec = require('cordova/exec');

type VideoResult = 'closed' | 'error';
type VideoCallback = (result: VideoResult) => void;

class YoutubeVideoPlayer {
  openVideo(YTid: string, callback?: VideoCallback): Promise<VideoResult> {
    return new Promise<VideoResult>((resolve, reject) => {
      exec(
        (result?: string) => {
          console.log(result);
          callback?.('closed');
          resolve('closed');
        },
        (error?: string) => {
          console.error(error);
          callback?.('error');
          reject(new Error(error ?? 'Unknown error opening video'));
        },
        'YoutubeVideoPlayer',
        'openVideo',
        [YTid],
      );
    });
  }
}

const youtubeVideoPlayer = new YoutubeVideoPlayer();
export = youtubeVideoPlayer;
