import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

const useLogin = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, Member>,
) => {
  return useMutation(
    ({ username, password }): Promise<AxiosResponse<string, string>> =>
      axios.post(
        '/login',
        {
          username,
          password,
        },
        { withCredentials: true },
      ),
    {
      ...options,
      onSettled(data) {
        if (data?.headers.Authorization) {
          axios.defaults.headers.common['Authorization'] = data?.headers.Authorization;
          localStorage.setItem('AccessToken', data.headers.Authorization);
        }
      },
    },
  );
};

export default useLogin;
