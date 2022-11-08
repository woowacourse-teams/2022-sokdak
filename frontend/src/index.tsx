import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClientProvider, QueryClient } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { BrowserRouter } from 'react-router-dom';

import { AuthContextProvider } from './context/Auth';
import { PaginationContextProvider } from './context/Pagination';
import { SnackBarContextProvider } from './context/Snackbar';

import App from './App';
import runJenniferFront from './jenniferFront';
import GlobalStyle from './style/GlobalStyle';
import theme from './style/theme';
import { ThemeProvider } from '@emotion/react';

runJenniferFront();

if (process.env.MODE === 'LOCAL:MSW') {
  const { worker } = require('./mocks/worker');
  worker.start({
    onUnhandledRequest: 'bypass',
  });
}

if (process.env.MODE !== 'LOCAL:MSW' && 'serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker
      .register('/service-worker.js')
      .then(registration => {
        console.log('SW registered: ', registration);
      })
      .catch(registrationError => {
        console.log('SW registration failed: ', registrationError);
      });
  });
}

const queryClient = new QueryClient({ defaultOptions: { queries: { suspense: true, useErrorBoundary: true } } });

const rootNode = document.getElementById('root') as Element;

ReactDOM.createRoot(rootNode).render(
  <React.StrictMode>
    <AuthContextProvider>
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <ThemeProvider theme={theme}>
            <SnackBarContextProvider>
              <PaginationContextProvider>
                <App />
              </PaginationContextProvider>
            </SnackBarContextProvider>
          </ThemeProvider>
          <ReactQueryDevtools />
        </QueryClientProvider>
      </BrowserRouter>
      <GlobalStyle />
    </AuthContextProvider>
  </React.StrictMode>,
);
