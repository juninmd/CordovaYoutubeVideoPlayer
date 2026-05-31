export default {
  preset: 'ts-jest',
  testEnvironment: 'node',
  testMatch: ['**/test/**/*.test.ts'],
  collectCoverageFrom: [
    'plugins/com.bunkerpalace.cordova.YoutubeVideoPlayer/www/*.ts',
    'www/**/*.ts',
  ],
  coverageThreshold: {
    global: {
      branches: 80,
      functions: 80,
      lines: 80,
      statements: 80,
    },
  },
  coverageDirectory: 'coverage',
  coverageReporters: ['text', 'lcov', 'clover'],
  transform: {
    '^.+\\.tsx?$': ['ts-jest', { useESM: true }],
  },
};
