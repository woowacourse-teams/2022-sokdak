import { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';

import AuthContext from '@/context/Auth';
import SnackbarContext from '@/context/Snackbar';

import PATH from '@/constants/path';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const PrivateRoute = () => {
  const { isLogin } = useContext(AuthContext);
  const { showSnackbar } = useContext(SnackbarContext);

  if (!isLogin) {
    showSnackbar(SNACKBAR_MESSAGE.NOT_LOGIN);
    return <Navigate to={PATH.LOGIN} />;
  }
  return <Outlet />;
};

export default PrivateRoute;
