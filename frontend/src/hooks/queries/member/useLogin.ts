import { useContext } from 'react';
import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import AuthContext from '@/context/Auth';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis';
import { STORAGE_KEY } from '@/constants/localStorage';
import SNACKBAR_MESSAGE from '@/constants/snackbar';
import { parseJwt } from '@/utils/decodeJwt';

const useLogin = (options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, Member>) => {
  const { showSnackbar } = useSnackbar();
  const { setIsLogin, setUserName } = useContext(AuthContext);
  return useMutation(
    ({ username, password }): Promise<AxiosResponse<never>> =>
      axios.post<never>(
        '/login',
        {
          username,
          password,
        },
        { withCredentials: true, headers: { 'Refresh-Token': localStorage.getItem(STORAGE_KEY.REFRESH_TOKEN)! } },
      ),
    {
      ...options,
      onSuccess(data, variables, context) {
        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_LOGIN);
        setIsLogin(true);

        const accessToken = data?.headers.authorization;
        const refreshToken = data?.headers['refresh-token'];
        if (accessToken) {
          authFetcher.defaults.headers.common['Authorization'] = accessToken;
          localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, accessToken);
          setUserName(parseJwt(accessToken)?.nickname!);
        }
        if (refreshToken) {
          authFetcher.defaults.headers.common['Refresh-Token'] = refreshToken;
          localStorage.setItem(STORAGE_KEY.REFRESH_TOKEN, refreshToken);
        }
        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
    },
  );
};

export default useLogin;
