import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import api from '@/apis';

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
      api.post('/members/signup', {
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
