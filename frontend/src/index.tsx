import { ThemeProvider } from '@emotion/react';
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { SnackBarContextProvider } from './context/Snackbar';
import GlobalStyle from './style/GlobalStyle';
import theme from './style/theme';
import { BrowserRouter } from 'react-router-dom';

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/worker');

  worker.start();
}

const rootNode = document.getElementById('root') as Element;

ReactDOM.createRoot(rootNode).render(
  <React.StrictMode>
    <SnackBarContextProvider>
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <App />
        </BrowserRouter>
        <GlobalStyle />
      </ThemeProvider>
    </SnackBarContextProvider>
  </React.StrictMode>,
);
