import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

const useLike = (
  options?: UseMutationOptions<
    AxiosResponse<{ like: boolean; likeCount: number }, string>,
    AxiosError<{ message: string }>,
    { id: string; boardId: number }
  >,
) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ id, boardId }): Promise<AxiosResponse<{ like: boolean; likeCount: number }, string>> =>
      authFetcher.put(`/posts/${id}/like`, { boardId }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.invalidateQueries(QUERY_KEYS.POST);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
      mutationKey: MUTATION_KEY.CREATE_LIKE,
    },
  );
};

export default useLike;
