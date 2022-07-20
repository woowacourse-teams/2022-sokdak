import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

const useLike = (
  options?: UseMutationOptions<
    AxiosResponse<{ like: boolean; likeCount: number }, string>,
    AxiosError<{ message: string }>,
    { id: string }
  >,
) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ id }): Promise<AxiosResponse<{ like: boolean; likeCount: number }, string>> => axios.put(`/posts/${id}/like`),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.refetchQueries(QUERY_KEYS.POSTS);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
    },
  );
};

export default useLike;
