//
//  Copyright (c) 2013-2014 Cédric Luthi. All rights reserved.
//

#import "XCDYouTubeVideoOperation.h"

#import "XCDYouTubeVideo+Private.h"
#import "XCDYouTubeError.h"
#import "XCDYouTubeVideoWebpage.h"
#import "XCDYouTubePlayerScript.h"

@interface XCDYouTubeVideoOperation () <NSURLSessionDataDelegate>
@property (atomic, copy, readonly) NSString *videoIdentifier;
@property (atomic, copy, readonly) NSString *languageIdentifier;

@property (atomic, strong) NSURLSession *session;
@property (atomic, strong) NSURLSessionDataTask *dataTask;
@property (atomic, strong) NSURLResponse *response;
@property (atomic, strong) NSMutableData *connectionData;
@property (atomic, assign) NSUInteger eventLabelIndex;

@property (atomic, assign) BOOL isExecuting;
@property (atomic, assign) BOOL isFinished;

@property (atomic, strong) XCDYouTubeVideoWebpage *webpage;
@property (atomic, strong) XCDYouTubeVideo *noStreamVideo;
@property (atomic, strong) NSError *lastError;
@property (atomic, strong) NSError *youTubeError;

@property (atomic, strong, readwrite) NSError *error;
@property (atomic, strong, readwrite) XCDYouTubeVideo *video;
@end

static NSString *const kEventLabels[] = { @"embedded", @"detailpage", @"vevo" };
static const NSUInteger kEventLabelCount = 3;

@implementation XCDYouTubeVideoOperation

- (instancetype) init
{
	@throw [NSException exceptionWithName:NSGenericException reason:@"Use the `initWithVideoIdentifier:languageIdentifier:` method instead." userInfo:nil];
}

- (instancetype) initWithVideoIdentifier:(NSString *)videoIdentifier languageIdentifier:(NSString *)languageIdentifier
{
	if (!(self = [super init]))
		return nil;

	_videoIdentifier = videoIdentifier ?: @"";
	_languageIdentifier = languageIdentifier ?: @"en";

	return self;
}

- (void) startNextVideoInfoRequest
{
	if (self.eventLabelIndex >= kEventLabelCount)
	{
		[self finishWithError:self.youTubeError ?: self.lastError];
	}
	else
	{
		NSString *eventLabel = kEventLabels[self.eventLabelIndex++];

		NSDictionary *query = @{ @"video_id": self.videoIdentifier, @"hl": self.languageIdentifier, @"el": eventLabel, @"ps": @"default" };
		NSString *queryString = XCDQueryStringWithDictionary(query, NSUTF8StringEncoding);
		NSURL *videoInfoURL = [NSURL URLWithString:[@"https://www.youtube.com/get_video_info?" stringByAppendingString:queryString]];
		[self startRequestWithURL:videoInfoURL];
	}
}

- (void) startRequestWithURL:(NSURL *)url
{
	if ([self isCancelled])
		return;

	NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10];
	[request setValue:self.languageIdentifier forHTTPHeaderField:@"Accept-Language"];

	if (!self.session)
	{
		self.session = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue]];
	}
	self.dataTask = [self.session dataTaskWithRequest:request];
	[self.dataTask resume];
}

- (void) handleVideoInfoResponseWithInfo:(NSDictionary *)info playerScript:(XCDYouTubePlayerScript *)playerScript
{
	NSError *error = nil;
	XCDYouTubeVideo *video = [[XCDYouTubeVideo alloc] initWithIdentifier:self.videoIdentifier info:info playerScript:playerScript response:self.response error:&error];
	if (video)
	{
		[video mergeVideo:self.noStreamVideo];
		[self finishWithVideo:video];
	}
	else
	{
		if ([error.domain isEqual:XCDYouTubeVideoErrorDomain] && error.code == XCDYouTubeErrorUseCipherSignature)
		{
			self.noStreamVideo = error.userInfo[XCDYouTubeNoStreamVideoUserInfoKey];

			NSDictionary *query = @{ @"v": self.videoIdentifier, @"hl": self.languageIdentifier, @"has_verified": @YES };
			NSString *queryString = XCDQueryStringWithDictionary(query, NSUTF8StringEncoding);
			NSURL *webpageURL = [NSURL URLWithString:[@"https://www.youtube.com/watch?" stringByAppendingString:queryString]];
			[self startRequestWithURL:webpageURL];
		}
		else
		{
			self.lastError = error;
			if (error.code > 0)
				self.youTubeError = error;

			[self startNextVideoInfoRequest];
		}
	}
}

- (void) handleWebPageResponse
{
	self.webpage = [[XCDYouTubeVideoWebpage alloc] initWithData:self.connectionData response:self.response];

	if (self.webpage.javaScriptPlayerURL)
		[self startRequestWithURL:self.webpage.javaScriptPlayerURL];
	else
		[self startNextVideoInfoRequest];
}

- (void) handleJavaScriptPlayerResponse
{
	NSString *script = [[NSString alloc] initWithData:self.connectionData encoding:NSISOLatin1StringEncoding];
	XCDYouTubePlayerScript *playerScript = [[XCDYouTubePlayerScript alloc] initWithString:script];

	if (playerScript)
		[self handleVideoInfoResponseWithInfo:self.webpage.videoInfo playerScript:playerScript];
	else
		[self startNextVideoInfoRequest];
}

- (void) finishWithVideo:(XCDYouTubeVideo *)video
{
	self.video = video;
	[self finish];
}

- (void) finishWithError:(NSError *)error
{
	self.error = error;
	[self finish];
}

#pragma mark - NSOperation

+ (BOOL) automaticallyNotifiesObserversForKey:(NSString *)key
{
	SEL selector = NSSelectorFromString(key);
	return selector == @selector(isExecuting) || selector == @selector(isFinished) || [super automaticallyNotifiesObserversForKey:key];
}

- (BOOL) isConcurrent
{
	return YES;
}

- (void) start
{
	if ([self isCancelled])
		return;

	self.isExecuting = YES;

	self.eventLabelIndex = 0;
	[self startNextVideoInfoRequest];
}

- (void) cancel
{
	[self.dataTask cancel];
	[self.session invalidateAndCancel];
	self.session = nil;
	[super cancel];

	[self finish];
}

- (void) finish
{
	self.isExecuting = NO;
	[self.session finishTasksAndInvalidate];
	self.session = nil;
	self.isFinished = YES;
}

#pragma mark - NSURLSessionDataDelegate

- (void) URLSession:(NSURLSession *)session dataTask:(NSURLSessionDataTask *)dataTask didReceiveResponse:(NSURLResponse *)response completionHandler:(void (^)(NSURLSessionResponseDisposition))completionHandler
{
	NSUInteger capacity = response.expectedContentLength == NSURLResponseUnknownLength ? 0 : (NSUInteger)response.expectedContentLength;
	self.connectionData = [[NSMutableData alloc] initWithCapacity:capacity];
	self.response = response;
	completionHandler(NSURLSessionResponseAllow);
}

- (void) URLSession:(NSURLSession *)session dataTask:(NSURLSessionDataTask *)dataTask didReceiveData:(NSData *)data
{
	[self.connectionData appendData:data];
}

- (void) URLSession:(NSURLSession *)session task:(NSURLSessionTask *)task didCompleteWithError:(NSError *)connectionError
{
	if (connectionError)
	{
		NSDictionary *userInfo = @{ NSLocalizedDescriptionKey: connectionError.localizedDescription,
		                            NSUnderlyingErrorKey: connectionError };
		self.lastError = [NSError errorWithDomain:XCDYouTubeVideoErrorDomain code:XCDYouTubeErrorNetwork userInfo:userInfo];
		[self startNextVideoInfoRequest];
		return;
	}

	NSURL *requestURL = task.originalRequest.URL;

	if ([requestURL.path isEqualToString:@"/get_video_info"])
	{
		NSString *videoQuery = [[NSString alloc] initWithData:self.connectionData encoding:NSASCIIStringEncoding];
		NSDictionary *info = XCDDictionaryWithQueryString(videoQuery, NSUTF8StringEncoding);
		[self handleVideoInfoResponseWithInfo:info playerScript:nil];
	}
	else if ([requestURL.path isEqualToString:@"/watch"])
	{
		[self handleWebPageResponse];
	}
	else
	{
		[self handleJavaScriptPlayerResponse];
	}

	[self.session finishTasksAndInvalidate];
	self.session = nil;
}

@end
