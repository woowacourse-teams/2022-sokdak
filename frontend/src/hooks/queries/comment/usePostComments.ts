import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createComment } from '@/api/comment';
import type { CreateCommentsRequest } from '@/api/comment';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

interface UsePostCommentsProps extends CreateCommentsRequest {
  id: string;
}

const usePostComments = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<{ message: string }>, UsePostCommentsProps>,
) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ content, anonymous, id }): Promise<AxiosResponse<null>> =>
      createComment(String(id), {
        content,
        anonymous,
      }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.refetchQueries(QUERY_KEYS.COMMENTS);
        queryClient.refetchQueries(QUERY_KEYS.POSTS);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
      mutationKey: MUTATION_KEY.POST_COMMENT,
    },
  );
};

export default usePostComments;
