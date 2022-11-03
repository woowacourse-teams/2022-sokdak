import { useContext } from 'react';
import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import AuthContext from '@/context/Auth';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/api/fetcher/auth';
import { requestGetLogout } from '@/api/member';
import { STORAGE_KEY } from '@/constants/localStorage';
import QUERY_KEYS from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useLogout = (options?: UseQueryOptions<AxiosResponse<null>, AxiosError<Error>, null, string>) => {
  const { showSnackbar } = useSnackbar();
  const { setIsLogin, setUsername } = useContext(AuthContext);
  return useQuery(QUERY_KEYS.LOGOUT, () => requestGetLogout(), {
    ...options,
    onSuccess(data) {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_LOGOUT);
      setIsLogin(false);
      setUsername('');
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
