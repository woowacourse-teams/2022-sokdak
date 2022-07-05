import React, { useState } from 'react';

type SnackbarContextType = {
  isVisible: boolean;
  message: string;
  showSnackbar: (message: string) => void;
};

const SnackbarContext = React.createContext<SnackbarContextType>({
  isVisible: false,
  message: '',
  showSnackbar: () => {},
});

export const SnackBarContextProvider = (props: any) => {
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
