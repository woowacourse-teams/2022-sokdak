import { useContext } from 'react';
import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import AuthContext from '@/context/Auth';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/api/fetcher/auth';
import { requestGetLogout } from '@/api/member';
import { STORAGE_KEY } from '@/constants/localStorage';
import QUERY_KEYS from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useLogout = (options?: UseQueryOptions<null, AxiosError<Error>, null, string>) => {
  const { showSnackbar } = useSnackbar();
  const { setIsLogin, setUsername } = useContext(AuthContext);

  return useQuery(QUERY_KEYS.LOGOUT, () => requestGetLogout(), {
    ...options,
    onSuccess(data) {
      setIsLogin(false);
      setUsername('');

      delete authFetcher.defaults.headers.common['Authorization'];
      localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
      localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);

      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_LOGOUT);

      if (options && options.onSuccess) {
        options.onSuccess(data);
      }
    },
    enabled: false,
    cacheTime: 0,
    suspense: false,
  });
};

export default useLogout;
