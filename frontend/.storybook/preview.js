import { QueryClient, QueryClientProvider } from 'react-query';

import { AuthContextProvider } from '../src/context/Auth';
import { SnackBarContextProvider } from '../src/context/Snackbar';
import GlobalStyle from '../src/style/GlobalStyle';
import theme from '../src/style/theme';
import { ThemeProvider } from '@emotion/react';

const queryClient = new QueryClient();

export const decorators = [
  Story => (
    <>
      <ThemeProvider theme={theme}>
        <QueryClientProvider client={queryClient}>
          <SnackBarContextProvider>
            <AuthContextProvider>
              <GlobalStyle />
              <Story />
            </AuthContextProvider>
          </SnackBarContextProvider>
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
