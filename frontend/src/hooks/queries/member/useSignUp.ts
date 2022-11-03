import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError } from 'axios';

import { createMember } from '@/api/member';
import type { CreateMemberRequest } from '@/api/member';

const useSignUp = (options?: UseMutationOptions<null, AxiosError, CreateMemberRequest>) => {
  return useMutation(
    ({ email, username, nickname, code, password, passwordConfirmation }) =>
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
