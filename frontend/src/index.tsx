import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClientProvider, QueryClient } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { BrowserRouter } from 'react-router-dom';

import axios from 'axios';

import { AuthContextProvider } from './context/Auth';
import { PaginationContextProvider } from './context/Pagination';
import { SnackBarContextProvider } from './context/Snackbar';

import authFetcher from './apis';
import { STORAGE_KEY } from './constants/localStorage';
import { isExpired, parseJwt } from './utils/decodeJwt';

import App from './App';
import runJenniferFront from './jenniferFront';
import GlobalStyle from './style/GlobalStyle';
import theme from './style/theme';
import { ThemeProvider } from '@emotion/react';

runJenniferFront();

if (process.env.MODE === 'LOCAL:MSW') {
  const { worker } = require('./mocks/worker');
  worker.start();
}

axios.defaults.baseURL = process.env.API_URL;
axios.defaults.withCredentials = true;

const queryClient = new QueryClient();

const rootNode = document.getElementById('root') as Element;

const refreshToken = localStorage.getItem(STORAGE_KEY.REFRESH_TOKEN);

if (refreshToken && isExpired(parseJwt(refreshToken)!)) {
  localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
  localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);
}

if (refreshToken && !isExpired(parseJwt(refreshToken)!)) {
  authFetcher.defaults.headers.common['Refresh-Token'] = refreshToken;
}

const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
if (accessToken) authFetcher.defaults.headers.common['Authorization'] = accessToken;

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
