import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

interface UseUpdateNicknameProps {
  nickname: string;
}

const useUpdateNickname = (
  options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, UseUpdateNicknameProps>,
) => {
  return useMutation(
    ({ nickname }): Promise<AxiosResponse> =>
      axios.patch('/members/nickname', {
        nickname,
      }),
    {
      ...options,
    },
  );
};

export default useUpdateNickname;
