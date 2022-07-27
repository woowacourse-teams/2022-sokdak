import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

const useVerificationCodeCheck = (
  options?: UseMutationOptions<
    AxiosResponse<string, string>,
    AxiosError<{ message: string }>,
    { email: string; code: string }
  >,
) => {
  return useMutation(
    ({ email, code }): Promise<AxiosResponse<string, string>> =>
      axios.post('/members/signup/email/verification', {
        email,
        code,
      }),
    {
      ...options,
    },
  );
};

export default useVerificationCodeCheck;
