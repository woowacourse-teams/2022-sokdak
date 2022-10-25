import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import api from '@/apis';

const useVerificationCodeCheck = (
  options?: UseMutationOptions<
    AxiosResponse<string, string>,
    AxiosError<{ message: string }>,
    { email: string; code: string }
  >,
) => {
  return useMutation(
    ({ email, code }): Promise<AxiosResponse<string, string>> =>
      api.post('/members/signup/email/verification', {
        email,
        code,
      }),
    {
      ...options,
    },
  );
};

export default useVerificationCodeCheck;
