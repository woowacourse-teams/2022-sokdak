import { StateAndAction } from 'sokdak-util-types';

import React, { useState } from 'react';

import { STORAGE_KEY } from '@/constants/localStorage';
import { parseJwt } from '@/utils/decodeJwt';

interface AuthContextValue extends StateAndAction<boolean, 'isLogin'>, StateAndAction<string, 'username'> {}
const AuthContext = React.createContext<AuthContextValue>({} as AuthContextValue);

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
  const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
  const [isLogin, setIsLogin] = useState(!!accessToken);
  const [username, setUsername] = useState(accessToken ? parseJwt(accessToken).nickname : '');

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUsername }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
