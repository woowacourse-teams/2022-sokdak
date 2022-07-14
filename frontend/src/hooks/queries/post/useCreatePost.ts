import { useContext } from 'react';
import { useQueryClient, useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

import QUERY_KEYS from '@/constants/queries';

const useCreatePost = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError, Pick<Post, 'title' | 'content'>>,
) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useContext(SnackbarContext);

  return useMutation(
    ({ title, content }: { title: string; content: string }): Promise<AxiosResponse<string, string>> =>
      axios.post('/posts', {
        title,
        content,
      }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.resetQueries(QUERY_KEYS.POSTS);
        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_WRITE_POST);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
    },
  );
};

export default useCreatePost;
