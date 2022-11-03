import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError } from 'axios';

import { requestUpdateLikePost, UpdateLikePostResponse } from '@/api/post';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

const useLike = (options?: UseMutationOptions<UpdateLikePostResponse, AxiosError, { id: string }>) => {
  const queryClient = useQueryClient();

  return useMutation(({ id }) => requestUpdateLikePost(String(id)), {
    ...options,
    onSuccess: (data, variables, context) => {
      queryClient.invalidateQueries(QUERY_KEYS.POST);

      if (options && options.onSuccess) {
        options.onSuccess(data, variables, context);
      }
    },
    mutationKey: MUTATION_KEY.CREATE_LIKE,
  });
};

export default useLike;
