import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';

interface UseUpdateNicknameProps {
  nickname: string;
}

const useUpdateNickname = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, UseUpdateNicknameProps>,
) => {
  return useMutation(
    ({ nickname }): Promise<AxiosResponse> =>
      authFetcher.patch('/members/nickname', {
        nickname,
      }),
    {
      ...options,
    },
  );
};

export default useUpdateNickname;
