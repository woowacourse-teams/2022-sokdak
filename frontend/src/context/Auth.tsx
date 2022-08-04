import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';

import authFetcher from '@/apis';
import { STORAGE_KEY } from '@/constants/localStorage';
import { parseJwt, isExpired } from '@/utils/decodeJwt';

interface AuthContextValue {
  isLogin: boolean;
  setIsLogin: Dispatch<SetStateAction<boolean>>;
  username: string;
  setUserName: Dispatch<SetStateAction<string>>;
}

const AuthContext = React.createContext<AuthContextValue>({} as AuthContextValue);

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
  const [isLogin, setIsLogin] = useState(false);
  const [username, setUserName] = useState('');

  useEffect(() => {
    const refreshToken = localStorage.getItem(STORAGE_KEY.REFRESH_TOKEN);
    if (refreshToken && isExpired(parseJwt(refreshToken)!)) {
      localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
      localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);
      return;
    }

    if (refreshToken) {
      authFetcher.defaults.headers.common['Refresh-Token'] = refreshToken;
    }
    const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
    if (accessToken) {
      setIsLogin(true);
      setUserName(parseJwt(accessToken)?.nickname!);
      authFetcher.defaults.headers.common['Authorization'] = accessToken;
    }
  }, []);

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUserName }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
