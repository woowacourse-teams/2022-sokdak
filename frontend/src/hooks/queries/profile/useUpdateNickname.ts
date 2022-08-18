import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis';

interface UseUpdateNicknameProps {
  nickname: string;
}

const useUpdateNickname = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, UseUpdateNicknameProps>,
) => {
  const { showSnackbar } = useSnackbar();

  return useMutation(
    ({ nickname }): Promise<AxiosResponse> =>
      authFetcher.patch('/members/nickname', {
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
    },
  );
};

export default useUpdateNickname;
