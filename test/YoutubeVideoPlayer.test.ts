import type YoutubeVideoPlayerType from '../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer';

let YoutubeVideoPlayer: typeof YoutubeVideoPlayerType;
const mockExec: jest.Mock<void, unknown[]> = jest.fn();

jest.mock('cordova/exec', () => mockExec, { virtual: true });

describe('YoutubeVideoPlayer', () => {
  beforeEach(async () => {
    jest.resetModules();
    mockExec.mockClear();
    const mod = await import('../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer');
    YoutubeVideoPlayer = mod.default;
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
    const successFn = mockExec.mock.calls[0][0] as () => void;
    successFn();
    await expect(promise).resolves.toBe('closed');
  });

  it('should invoke error with "error" when exec fails', async () => {
    const promise = YoutubeVideoPlayer.openVideo('test123');
    const errorFn = mockExec.mock.calls[0][1] as () => void;
    errorFn();
    await expect(promise).rejects.toBe('error');
  });

  it('should handle openVideo success and error callbacks gracefully', async () => {
    const promise = YoutubeVideoPlayer.openVideo('test123');
    const successFn = mockExec.mock.calls[0][0] as () => void;
    expect(() => successFn()).not.toThrow();
    await expect(promise).resolves.toBe('closed');

    const promise2 = YoutubeVideoPlayer.openVideo('test456');
    const errorFn2 = mockExec.mock.calls[1][1] as () => void;
    expect(() => errorFn2()).not.toThrow();
    await expect(promise2).rejects.toBe('error');
  });

  it('should be a singleton instance', async () => {
    const mod = await import('../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer');
    expect(mod.default).toBe(YoutubeVideoPlayer);
  });

  it('should call exec with correct service and action names', () => {
    YoutubeVideoPlayer.openVideo('abc123');
    const [, , service, action] = mockExec.mock.calls[0];
    expect(service).toBe('YoutubeVideoPlayer');
    expect(action).toBe('openVideo');
  });
});
