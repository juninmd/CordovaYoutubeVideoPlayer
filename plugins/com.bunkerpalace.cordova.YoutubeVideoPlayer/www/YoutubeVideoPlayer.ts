import exec from 'cordova/exec';

export type VideoResult = 'closed' | 'error';

const YoutubeVideoPlayer = {
  openVideo(videoId: string): Promise<VideoResult> {
    return new Promise<VideoResult>((resolve, reject) => {
      exec(
        (_result: unknown) => resolve('closed'),
        (_error: unknown) => reject('error'),
        'YoutubeVideoPlayer',
        'openVideo',
        [videoId]
      );
    });
  },
};

export default YoutubeVideoPlayer;
