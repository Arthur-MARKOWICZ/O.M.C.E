module.exports = {
  preset: 'jest-expo',
  testMatch: ['**/__tests__/**/*.test.{ts,tsx}'],
  moduleNameMapper: { '^@/(.*)$': '<rootDir>/$1' },
  // pnpm nests React Native packages under .pnpm, so match the full path before ignoring transforms.
  transformIgnorePatterns: ['node_modules/(?!.*(react-native|@react-native|expo|@expo|@react-navigation|react-navigation|unimodules|native-base|react-native-svg))']
};
