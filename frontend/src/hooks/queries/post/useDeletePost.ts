import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis/authFetcher';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useDeletePost = (options?: UseMutationOptions<AxiosResponse, AxiosError, string>) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useSnackbar();

  return useMutation(
    id => {
      return authFetcher.delete(`/posts/${id}`);
    },
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.invalidateQueries(QUERY_KEYS.POSTS);
        queryClient.invalidateQueries(QUERY_KEYS.POSTS_BY_BOARDS);
        queryClient.invalidateQueries(QUERY_KEYS.MY_POSTS);

        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_DELETE_POST);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
      mutationKey: MUTATION_KEY.DELETE_POST,
    },
  );
};

export default useDeletePost;
