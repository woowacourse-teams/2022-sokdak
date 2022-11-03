import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError } from 'axios';

import { createReply } from '@/api/comment';
import type { CreateReplyRequest } from '@/api/comment';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

interface UseCreateReplyProps extends CreateReplyRequest {
  commentId: number | string;
}

const useCreateReply = (options?: UseMutationOptions<null, AxiosError<Error>, UseCreateReplyProps>) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ commentId, content, anonymous }) =>
      createReply(String(commentId), {
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
      mutationKey: MUTATION_KEY.CREATE_REPLY,
    },
  );
};

export default useCreateReply;
