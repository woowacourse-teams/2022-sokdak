import { useContext } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import AuthContext from '@/context/Auth';
import SnackbarContext from '@/context/Snackbar';

const PublicRoute = () => {
  const { isLogin } = useContext(AuthContext);
  const { showSnackbar } = useContext(SnackbarContext);
  const navigate = useNavigate();

  if (isLogin) {
    showSnackbar('이미 로그인하였습니다');
    navigate(-1);
  }
  return <Outlet />;
};

export default PublicRoute;
