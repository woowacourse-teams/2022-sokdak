import React, { useState } from 'react';

interface SnackbarContextValue {
  isVisible: boolean;
  message: string;
  showSnackbar: (message: string) => void;
}

const SnackbarContext = React.createContext<SnackbarContextValue>({} as SnackbarContextValue);

export const SnackBarContextProvider = (props: { children: React.ReactNode }) => {
  const [isVisible, setIsVisible] = useState(false);
  const [message, setMessage] = useState('');
  let timer: NodeJS.Timeout;

  const showSnackbar = (message: string) => {
    setMessage(message);
    setIsVisible(true);

    timer = setTimeout(() => {
      hideSnackbar();
    }, 1500);
  };

  const hideSnackbar = () => {
    setMessage('');
    setIsVisible(false);
    clearTimeout(timer);
  };

  return (
    <SnackbarContext.Provider value={{ isVisible, message, showSnackbar }}>{props.children}</SnackbarContext.Provider>
  );
};

export default SnackbarContext;
