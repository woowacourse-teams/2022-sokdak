import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createMember } from '@/api/member';
import type { CreateMemberRequest } from '@/api/member';

const useSignUp = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<{ message: string }>, CreateMemberRequest>,
) => {
  return useMutation(
    ({ email, username, nickname, code, password, passwordConfirmation }): Promise<AxiosResponse<null>> =>
      createMember({
        email,
        username,
        nickname,
        code,
        password,
        passwordConfirmation,
      }),
    {
      ...options,
    },
  );
};

export default useSignUp;
