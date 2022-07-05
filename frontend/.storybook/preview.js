import { ThemeProvider } from '@emotion/react';
import GlobalStyle from '../src/style/GlobalStyle';
import theme from '../src/style/theme';

export const decorators = [
  Story => (
    <>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <Story />
      </ThemeProvider>
    </>
  ),
];

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
    },
  },
};
