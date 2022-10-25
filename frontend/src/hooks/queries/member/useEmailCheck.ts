import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import api from '@/apis';

const useEmailCheck = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, { email: string }>,
) => {
  return useMutation(
    ({ email }): Promise<AxiosResponse<string, string>> =>
      api.post('/members/signup/email', {
        email,
      }),
    {
      ...options,
    },
  );
};

export default useEmailCheck;
