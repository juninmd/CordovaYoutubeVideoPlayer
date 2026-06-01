'use strict';
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { 'default': mod };
};
Object.defineProperty(exports, '__esModule', { value: true });
const exec_1 = __importDefault(require('cordova/exec'));
exports.default = {
  openVideo: (YTid) => {
    return new Promise((resolve, reject) => {
      (0, exec_1.default)(() => resolve('closed'), () => reject('error'), 'YoutubeVideoPlayer', 'openVideo', [YTid]);
    });
  },
};
//# sourceMappingURL=YoutubeVideoPlayer.js.map