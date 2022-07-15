import { useContext } from 'react';
import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

import QUERY_KEYS from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useDeletePost = (options?: UseMutationOptions<AxiosResponse, AxiosError, string>) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useContext(SnackbarContext);

  return useMutation(
    id => {
      return axios.delete(`/posts/${id}`);
    },
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.resetQueries(QUERY_KEYS.POSTS);
        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_DELETE_POST);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
    },
  );
};

export default useDeletePost;
