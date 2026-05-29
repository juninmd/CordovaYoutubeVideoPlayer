#import "YoutubeVideoPlayer.h"
#import "XCDYouTubeKit.h"

@implementation YoutubeVideoPlayer

- (void)openVideo:(CDVInvokedUrlCommand*)command
{
    NSString* videoID = [command.arguments objectAtIndex:0];

    if (videoID == nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                          messageAsString:@"Missing videoID Argument"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    XCDYouTubeVideoPlayerViewController *videoPlayerViewController =
        [[XCDYouTubeVideoPlayerViewController alloc] initWithVideoIdentifier:videoID];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayerPlaybackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification
                                               object:videoPlayerViewController.moviePlayer];

    [self.viewController presentMoviePlayerViewControllerAnimated:videoPlayerViewController];
    _eventsCallbackId = command.callbackId;
}

- (void)moviePlayerPlaybackDidFinish:(NSNotification *)notification
{
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:notification.object];

    MPMovieFinishReason finishReason = [notification.userInfo[MPMoviePlayerPlaybackDidFinishReasonUserInfoKey] integerValue];

    CDVPluginResult* pluginResult;
    if (finishReason == MPMovieFinishReasonPlaybackError) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                         messageAsString:@"Playback Error"];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:_eventsCallbackId];
}

@end
