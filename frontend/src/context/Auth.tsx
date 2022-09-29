import { StateAndAction } from 'sokdak-util-types';

import React, { useState } from 'react';

import { STORAGE_KEY } from '@/constants/localStorage';
import { parseJwt } from '@/utils/decodeJwt';

interface AuthContextValue extends StateAndAction<boolean, 'isLogin'>, StateAndAction<string, 'username'> {}
const AuthContext = React.createContext<AuthContextValue>({} as AuthContextValue);

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
  const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
  const [isLogin, setIsLogin] = useState(() => {
    if (accessToken) {
      return true;
    }
    return false;
  });

  const [username, setUsername] = useState(() => {
    if (accessToken) {
      return parseJwt(accessToken)?.nickname!;
    }
    return '';
  });

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUsername }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
