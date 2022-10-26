import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createMember } from '@/apis/member';

interface SignUpProps extends Member {
  email: string | null;
  nickname: string;
  code: string | null;
  passwordConfirmation: string;
}

const useSignUp = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, SignUpProps>,
) => {
  return useMutation(
    ({ email, username, nickname, code, password, passwordConfirmation }): Promise<AxiosResponse<string, string>> =>
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
