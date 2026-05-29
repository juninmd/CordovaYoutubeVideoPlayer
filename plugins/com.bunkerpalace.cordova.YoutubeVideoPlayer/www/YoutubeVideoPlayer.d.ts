type VideoResult = 'closed' | 'error';
type VideoCallback = (result: VideoResult) => void;
declare class YoutubeVideoPlayer {
    openVideo(YTid: string, callback?: VideoCallback): Promise<VideoResult>;
}
declare const youtubeVideoPlayer: YoutubeVideoPlayer;
export = youtubeVideoPlayer;
//# sourceMappingURL=YoutubeVideoPlayer.d.ts.map