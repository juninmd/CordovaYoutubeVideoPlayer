let YoutubeVideoPlayer: any;
const mockExec = jest.fn();

jest.mock('cordova/exec', () => mockExec, { virtual: true });

function loadModule() {
  return require('../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer').default;
}

describe('YoutubeVideoPlayer', () => {
  beforeEach(() => {
    jest.resetModules();
    mockExec.mockClear();
    YoutubeVideoPlayer = loadModule();
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

  it('should invoke success callback with "closed" when exec succeeds', async () => {
    const promise = YoutubeVideoPlayer.openVideo('test123');
    const successFn = mockExec.mock.calls[0][0];
    successFn();
    await expect(promise).resolves.toBe('closed');
  });

  it('should invoke error with "error" when exec fails', async () => {
    const promise = YoutubeVideoPlayer.openVideo('test123');
    const errorFn = mockExec.mock.calls[0][1];
    errorFn('some error');
    await expect(promise).rejects.toBe('error');
  });

  it('should handle openVideo without a callback', async () => {
    const promise = YoutubeVideoPlayer.openVideo('test123');
    const successFn = mockExec.mock.calls[0][0];
    expect(() => successFn()).not.toThrow();
    await expect(promise).resolves.toBe('closed');

    const errorFn = mockExec.mock.calls[0][1];
    expect(() => errorFn('err')).not.toThrow();
  });

  it('should be a singleton instance', () => {
    expect(loadModule()).toBe(YoutubeVideoPlayer);
  });

  it('should call exec with correct service and action names', () => {
    YoutubeVideoPlayer.openVideo('abc123');
    const [, , service, action] = mockExec.mock.calls[0];
    expect(service).toBe('YoutubeVideoPlayer');
    expect(action).toBe('openVideo');
  });
});