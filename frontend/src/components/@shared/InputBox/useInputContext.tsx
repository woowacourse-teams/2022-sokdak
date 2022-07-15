import React from 'react';

import { useInput } from './useInput';

interface InputContextProviderProps {
  children: React.ReactNode;
  value?: ReturnType<typeof useInput>;
}

const InputContext = React.createContext<ReturnType<typeof useInput> | undefined>(undefined);

const InputContextProvider = ({ children, value }: InputContextProviderProps) => {
  return <InputContext.Provider value={value}>{children}</InputContext.Provider>;
};

const useInputContext = () => {
  const context = React.useContext(InputContext);
  if (context === undefined) {
    throw new Error('not existed InputContext');
  }
  return context;
};

export { InputContextProvider, useInputContext };
