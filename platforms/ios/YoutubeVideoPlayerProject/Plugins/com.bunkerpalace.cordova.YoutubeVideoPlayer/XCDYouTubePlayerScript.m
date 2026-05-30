//
//  Copyright (c) 2013-2014 Cédric Luthi. All rights reserved.
//

#import "XCDYouTubePlayerScript.h"

#import <JavaScriptCore/JavaScriptCore.h>

static NSRegularExpression *sSignatureRegex;

@interface XCDYouTubePlayerScript ()
@property (nonatomic, strong) JSContext *context;
@property (nonatomic, strong) JSValue *signatureFunction;
@end

@implementation XCDYouTubePlayerScript

+ (void)initialize
{
	static dispatch_once_t once;
	dispatch_once(&once, ^{
		sSignatureRegex = [NSRegularExpression regularExpressionWithPattern:@"signature\\s*=\\s*([^\\(]+)" options:NSRegularExpressionCaseInsensitive error:NULL];
	});
}

- (instancetype) initWithString:(NSString *)string
{
	if (!(self = [super init]))
		return nil;

	NSString *script = [string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
	NSString *jsPrologue = @"(function()";
	NSString *jsEpilogue = @")();";
	if ([script hasPrefix:jsPrologue] && [script hasSuffix:jsEpilogue])
		script = [script substringWithRange:NSMakeRange(jsPrologue.length, script.length - (jsPrologue.length + jsEpilogue.length))];

	__block NSString *signatureFunctionName = nil;
	[sSignatureRegex enumerateMatchesInString:script options:(NSMatchingOptions)0 range:NSMakeRange(0, script.length) usingBlock:^(NSTextCheckingResult *result, NSMatchingFlags flags, BOOL *stop) {
		signatureFunctionName = [script substringWithRange:[result rangeAtIndex:1]];
		*stop = YES;
	}];

	if (!signatureFunctionName)
		return nil;

	_context = [[JSContext alloc] init];
	_context[@"window"] = @"window";
	_context[@"document"] = @"document";
	[_context evaluateScript:script];

	JSValue *fn = _context[signatureFunctionName];
	if (![fn isObject] || fn.isUndefined)
		return nil;

	_signatureFunction = fn;

	return self;
}

- (NSString *) unscrambleSignature:(NSString *)scrambledSignature
{
	if (!scrambledSignature)
		return nil;

	JSValue *result = [self.signatureFunction callWithArguments:@[scrambledSignature]];
	if (result.isString)
		return [result toString];

	return nil;
}

@end
