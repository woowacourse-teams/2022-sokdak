import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { requestUpdateLikePost } from '@/apis/post';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

const useLike = (
  options?: UseMutationOptions<
    AxiosResponse<{ like: boolean; likeCount: number }, string>,
    AxiosError<{ message: string }>,
    { id: string }
  >,
) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ id }): Promise<AxiosResponse<{ like: boolean; likeCount: number }, string>> => requestUpdateLikePost(String(id)),
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
