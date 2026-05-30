//
//  YoutubeVideoPlayer.m
//
//  Created by Adrien Girbone on 15/04/2014.
//
//

#import "YoutubeVideoPlayer.h"
#import "XCDYouTubeKit.h"

@implementation YoutubeVideoPlayer

- (void)openVideo:(CDVInvokedUrlCommand*)command
{
    NSString* videoID = [command.arguments objectAtIndex:0];

    if (videoID != nil) {
        XCDYouTubeVideoPlayerViewController *videoPlayerViewController =
            [[XCDYouTubeVideoPlayerViewController alloc] initWithVideoIdentifier:videoID];
        [[NSNotificationCenter defaultCenter] addObserver:self
            selector:@selector(moviePlayerPlaybackDidFinish:)
            name:MPMoviePlayerPlaybackDidFinishNotification
            object:videoPlayerViewController.moviePlayer];

        [self.viewController presentMoviePlayerViewControllerAnimated:videoPlayerViewController];
    } else {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
            messageAsString:@"Missing videoID Argument"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }

    _eventsCallbackId = command.callbackId;
}

- (void) moviePlayerPlaybackDidFinish:(NSNotification *)notification
{
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [[NSNotificationCenter defaultCenter] removeObserver:self
        name:MPMoviePlayerPlaybackDidFinishNotification
        object:notification.object];

    MPMovieFinishReason finishReason = [notification.userInfo[MPMoviePlayerPlaybackDidFinishReasonUserInfoKey] integerValue];
    if (finishReason == MPMovieFinishReasonPlaybackError) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
            messageAsString:@"Playback Error"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:_eventsCallbackId];
}

@end
