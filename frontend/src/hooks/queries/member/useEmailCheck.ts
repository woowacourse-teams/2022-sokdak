import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

const useEmailCheck = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, { email: string }>,
) => {
  return useMutation(
    ({ email }): Promise<AxiosResponse<string, string>> =>
      axios.post('/members/signup/email', {
        email,
      }),
    {
      ...options,
    },
  );
};

export default useEmailCheck;
