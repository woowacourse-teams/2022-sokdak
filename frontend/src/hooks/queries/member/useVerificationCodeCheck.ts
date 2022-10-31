import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createVerificationCodeCheck } from '@/api/member';
import { CreateVerificationCodeCheckRequest } from '@/api/member';

const useVerificationCodeCheck = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<Error>, CreateVerificationCodeCheckRequest>,
) => {
  return useMutation(({ email, code }) => createVerificationCodeCheck({ email, code }), {
    ...options,
  });
};

export default useVerificationCodeCheck;
