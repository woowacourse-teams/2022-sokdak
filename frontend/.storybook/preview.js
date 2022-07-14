import { ThemeProvider } from '@emotion/react';
import { QueryClient, QueryClientProvider } from 'react-query';
import GlobalStyle from '../src/style/GlobalStyle';
import theme from '../src/style/theme';

const queryClient = new QueryClient();

export const decorators = [
  Story => (
    <>
      <ThemeProvider theme={theme}>
        <QueryClientProvider client={queryClient}>
          <GlobalStyle />
          <Story />
        </QueryClientProvider>
      </ThemeProvider>
    </>
  ),
];

if (typeof global.process === 'undefined') {
  const { worker } = require('../src/mocks/worker');
  worker.start();
}

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
    },
  },
};
