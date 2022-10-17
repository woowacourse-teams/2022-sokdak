import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';

interface PostCommentsProps {
  content: string;
  anonymous: boolean;
  id: string;
  boardId: number;
}

const usePostComments = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, PostCommentsProps>,
) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ content, anonymous, id, boardId }): Promise<AxiosResponse<string, string>> =>
      authFetcher.post(`posts/${id}/comments`, {
        boardId,
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
