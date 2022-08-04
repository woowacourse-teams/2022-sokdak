import { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';

import AuthContext from '@/context/Auth';
import SnackbarContext from '@/context/Snackbar';

import PATH from '@/constants/path';

const PrivateRoute = () => {
  const { isLogin } = useContext(AuthContext);
  const { showSnackbar } = useContext(SnackbarContext);

  if (!isLogin) {
    showSnackbar('로그인이 필요한 서비스 입니다.');
    return <Navigate to={PATH.LOGIN} />;
  }
  return <Outlet />;
};

export default PrivateRoute;
