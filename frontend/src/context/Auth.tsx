import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';

import { STORAGE_KEY } from '@/constants/localStorage';
import { parseJwt } from '@/utils/decodeJwt';

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
    const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);

    if (accessToken) {
      setIsLogin(true);
      setUserName(parseJwt(accessToken)?.nickname!);
    }
  }, []);

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUserName }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
