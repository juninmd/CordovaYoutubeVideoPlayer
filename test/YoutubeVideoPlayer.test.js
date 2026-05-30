const mockExec = jest.fn();

jest.mock('cordova/exec', () => mockExec, { virtual: true });

describe('YoutubeVideoPlayer', () => {
  let YoutubeVideoPlayer;

  beforeEach(() => {
    jest.resetModules();
    mockExec.mockClear();
    YoutubeVideoPlayer = require('../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer');
  });

  it('should export an object with openVideo method', () => {
    expect(YoutubeVideoPlayer).toBeDefined();
    expect(typeof YoutubeVideoPlayer.openVideo).toBe('function');
  });

  it('should call cordova/exec with correct arguments', () => {
    const videoId = 'dQw4w9WgXcQ';
    YoutubeVideoPlayer.openVideo(videoId);
    expect(mockExec).toHaveBeenCalledTimes(1);
    const callArgs = mockExec.mock.calls[0];
    expect(callArgs[0]).toBeInstanceOf(Function);
    expect(callArgs[1]).toBeInstanceOf(Function);
    expect(callArgs[2]).toBe('YoutubeVideoPlayer');
    expect(callArgs[3]).toBe('openVideo');
    expect(callArgs[4]).toEqual([videoId]);
  });

  it('should invoke success callback with "closed" when exec succeeds', () => {
    const callback = jest.fn();
    YoutubeVideoPlayer.openVideo('test123', callback);
    const successFn = mockExec.mock.calls[0][0];
    successFn();
    expect(callback).toHaveBeenCalledWith('closed');
  });

  it('should invoke error callback with "error" when exec fails', () => {
    const callback = jest.fn();
    YoutubeVideoPlayer.openVideo('test123', callback);
    const errorFn = mockExec.mock.calls[0][1];
    errorFn('some error');
    expect(callback).toHaveBeenCalledWith('error');
  });

  it('should handle openVideo without a callback', () => {
    YoutubeVideoPlayer.openVideo('test123');
    const successFn = mockExec.mock.calls[0][0];
    expect(() => successFn()).not.toThrow();
    const errorFn = mockExec.mock.calls[0][1];
    expect(() => errorFn('err')).not.toThrow();
  });

  it('should be a singleton instance', () => {
    const mod = require('../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer');
    expect(mod).toBe(YoutubeVideoPlayer);
  });

});
