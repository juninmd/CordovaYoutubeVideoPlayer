"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
let YoutubeVideoPlayer;
const mockExec = jest.fn();
jest.mock('cordova/exec', () => mockExec, { virtual: true });
function loadModule() {
    return __awaiter(this, void 0, void 0, function* () {
        // Using dynamic import to handle ES modules
        const module = yield Promise.resolve().then(() => __importStar(require('../plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/YoutubeVideoPlayer')));
        return module.default;
    });
}
describe('YoutubeVideoPlayer', () => {
    beforeEach(() => __awaiter(void 0, void 0, void 0, function* () {
        jest.resetModules();
        mockExec.mockClear();
        YoutubeVideoPlayer = yield loadModule();
    }));
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
    it('should invoke success callback with "closed" when exec succeeds', () => __awaiter(void 0, void 0, void 0, function* () {
        const promise = YoutubeVideoPlayer.openVideo('test123');
        const successFn = mockExec.mock.calls[0][0];
        successFn();
        yield expect(promise).resolves.toBe('closed');
    }));
    it('should invoke error with "error" when exec fails', () => __awaiter(void 0, void 0, void 0, function* () {
        const promise = YoutubeVideoPlayer.openVideo('test123');
        const errorFn = mockExec.mock.calls[0][1];
        errorFn('some error');
        yield expect(promise).rejects.toBe('error');
    }));
    it('should handle openVideo without a callback', () => __awaiter(void 0, void 0, void 0, function* () {
        const promise = YoutubeVideoPlayer.openVideo('test123');
        const successFn = mockExec.mock.calls[0][0];
        expect(() => successFn()).not.toThrow();
        yield expect(promise).resolves.toBe('closed');
        const errorFn = mockExec.mock.calls[0][1];
        expect(() => errorFn('err')).not.toThrow();
    }));
    it('should be a singleton instance', () => __awaiter(void 0, void 0, void 0, function* () {
        const secondInstance = yield loadModule();
        expect(secondInstance).toBe(YoutubeVideoPlayer);
    }));
    it('should call exec with correct service and action names', () => {
        YoutubeVideoPlayer.openVideo('abc123');
        const [, , service, action] = mockExec.mock.calls[0];
        expect(service).toBe('YoutubeVideoPlayer');
        expect(action).toBe('openVideo');
    });
});
//# sourceMappingURL=YoutubeVideoPlayer.test.js.map