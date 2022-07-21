import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';

import axios from 'axios';

interface AuthContextValue {
  isLogin: boolean;
  setIsLogin: Dispatch<SetStateAction<boolean>>;

  username: string;
  setUserName: Dispatch<SetStateAction<string>>;
}

const AuthContext = React.createContext<AuthContextValue>({} as AuthContextValue);

// 자동로그인 구현 필요

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
  const [isLogin, setIsLogin] = useState(false);
  const [username, setUserName] = useState('');

  useEffect(() => {
    const accessToken = localStorage.getItem('AccessToken');
    if (accessToken) {
      setIsLogin(true);
      setUserName('자동로그인');
      axios.defaults.headers.common['Authorization'] = accessToken;
    }
  }, []);

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUserName }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
