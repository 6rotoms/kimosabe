module.exports = {
  transform: {
    '^.+\\.jsx?$': '<rootDir>/jest-preprocess.js',
  },
  moduleNameMapper: {
    '.+\\.(css|styl|less|sass|scss)$': 'identity-obj-proxy',
  },
  testPathIgnorePatterns: ['node_modules', '\\.cache', '<rootDir>.*/public'],
  transformIgnorePatterns: ['node_modules/(?!(gatsby)/)'],
  globals: {
    __PATH_PREFIX__: '',
    __BASE_PATH__: '',
  },
  testURL: 'http://localhost',
  testRegex: '(/^test/.*|\\.(test|spec))\\.(js)$',
  setupFiles: ['<rootDir>/loadershim.js'],
};
