import { useContext } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import AuthContext from '@/context/Auth';
import SnackbarContext from '@/context/Snackbar';

import SNACKBAR_MESSAGE from '@/constants/snackbar';

const PublicRoute = () => {
  const { isLogin } = useContext(AuthContext);
  const { showSnackbar } = useContext(SnackbarContext);
  const navigate = useNavigate();

  if (isLogin) {
    showSnackbar(SNACKBAR_MESSAGE.ALREADY_LOGIN);
    navigate(-1);
  }
  return <Outlet />;
};

export default PublicRoute;
