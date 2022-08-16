import React, { Dispatch, SetStateAction, useState } from 'react';

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
  const accessToken = localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
  const [isLogin, setIsLogin] = useState(() => {
    if (accessToken) {
      return true;
    }
    return false;
  });

  const [username, setUserName] = useState(() => {
    if (accessToken) {
      return parseJwt(accessToken)?.nickname!;
    }
    return '';
  });

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUserName }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
