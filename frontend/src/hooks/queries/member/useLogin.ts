import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

const useLogin = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, Member>,
) => {
  return useMutation(
    ({ username, password }): Promise<AxiosResponse<string, string>> =>
      axios.post('/login', {
        username,
        password,
      }),
    {
      ...options,
    },
  );
};

export default useLogin;
