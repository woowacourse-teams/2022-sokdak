import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createReply } from '@/apis/comment';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

interface RequestProps {
  commentId: number | string;
  content: string;
  anonymous: boolean;
}

const useCreateReply = (options?: UseMutationOptions<AxiosResponse, AxiosError<ErrorResponse>, RequestProps>) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ commentId, content, anonymous }): Promise<AxiosResponse> =>
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
