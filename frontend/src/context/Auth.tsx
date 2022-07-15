import React, { Dispatch, SetStateAction, useState } from 'react';

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

  return <AuthContext.Provider value={{ isLogin, setIsLogin, username, setUserName }}>{children}</AuthContext.Provider>;
};

export default AuthContext;
