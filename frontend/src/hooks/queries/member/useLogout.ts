import { useContext } from 'react';
import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import AuthContext from '@/context/Auth';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis';
import { STORAGE_KEY } from '@/constants/localStorage';
import QUERY_KEYS from '@/constants/queries';

const useLogout = (options?: UseQueryOptions<AxiosResponse<never>, AxiosError<{ message: string }>, never, string>) => {
  const { showSnackbar } = useSnackbar();
  const { setIsLogin, setUserName } = useContext(AuthContext);
  return useQuery(QUERY_KEYS.LOGOUT, () => authFetcher.get<never>('/logout'), {
    ...options,
    onSuccess(data) {
      showSnackbar('로그아웃에 성공하였습니다.');
      setIsLogin(false);
      setUserName('');
      authFetcher.defaults.headers.common = {};

      localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, '');
      localStorage.setItem(STORAGE_KEY.REFRESH_TOKEN, '');
      if (options && options.onSuccess) {
        options.onSuccess(data);
      }
    },
    enabled: false,
  });
};

export default useLogout;
