import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClientProvider, QueryClient } from 'react-query';
import { BrowserRouter } from 'react-router-dom';

import { SnackBarContextProvider } from './context/Snackbar';

import App from './App';
import GlobalStyle from './style/GlobalStyle';
import theme from './style/theme';
import { ThemeProvider } from '@emotion/react';

if (process.env.MODE === 'LOCAL:MSW') {
  const { worker } = require('./mocks/worker');
  worker.start();
}

const queryClient = new QueryClient();
const rootNode = document.getElementById('root') as Element;

ReactDOM.createRoot(rootNode).render(
  <React.StrictMode>
    <SnackBarContextProvider>
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <QueryClientProvider client={queryClient}>
            <App />
          </QueryClientProvider>
        </BrowserRouter>
        <GlobalStyle />
      </ThemeProvider>
    </SnackBarContextProvider>
  </React.StrictMode>,
);
