import { useContext } from 'react';
import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import AuthContext from '@/context/Auth';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis/authFetcher';
import { requestUpdateNickname } from '@/apis/member';
import { STORAGE_KEY } from '@/constants/localStorage';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface UseUpdateNicknameProps {
  nickname: string;
}

const useUpdateNickname = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, UseUpdateNicknameProps>,
) => {
  const { setUsername } = useContext(AuthContext);
  const { showSnackbar } = useSnackbar();

  return useMutation(
    ({ nickname }): Promise<AxiosResponse> =>
      requestUpdateNickname({
        nickname,
      }),
    {
      ...options,
      onError: (error, variables, context) => {
        if (options && options.onError) {
          options.onError(error, variables, context);
        }
        showSnackbar(error.response?.data.message!);
      },
      onSuccess: (data, variables, context) => {
        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }

        const accessToken = data?.headers.authorization;

        if (accessToken) {
          authFetcher.defaults.headers.common['Authorization'] = accessToken;
          localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, accessToken);
          setUsername(variables.nickname);
          showSnackbar(SNACKBAR_MESSAGE.SUCCESS_UPDATE_NICKNAME);
        }
      },
    },
  );
};

export default useUpdateNickname;
