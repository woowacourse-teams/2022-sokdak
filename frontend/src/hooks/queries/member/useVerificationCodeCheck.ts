import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createVerificationCodeCheck } from '@/apis/member';

const useVerificationCodeCheck = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<{ message: string }>, { email: string; code: string }>,
) => {
  return useMutation(({ email, code }) => createVerificationCodeCheck({ email, code }), {
    ...options,
  });
};

export default useVerificationCodeCheck;
