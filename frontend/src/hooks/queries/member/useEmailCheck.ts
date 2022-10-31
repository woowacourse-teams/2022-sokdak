import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createEmailCheck } from '@/api/member';

const useEmailCheck = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, { email: string }>,
) => {
  return useMutation(
    ({ email }): Promise<AxiosResponse<string, string>> =>
      createEmailCheck({
        email,
      }),
    {
      ...options,
    },
  );
};

export default useEmailCheck;
