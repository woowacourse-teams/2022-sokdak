import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

interface PostCommentsProps {
  content: string;
  anonymous: boolean;
  id: string;
}

const usePostComments = (
  options?: UseMutationOptions<AxiosResponse<string, string>, AxiosError<{ message: string }>, PostCommentsProps>,
) => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ content, anonymous, id }): Promise<AxiosResponse<string, string>> =>
      axios.post(`posts/${id}/comments`, {
        content,
        anonymous,
      }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.refetchQueries(QUERY_KEYS.COMMENTS);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
    },
  );
};

export default usePostComments;
