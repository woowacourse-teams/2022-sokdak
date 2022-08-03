import { useQueryClient, useMutation, UseMutationOptions } from 'react-query';
import { useLocation } from 'react-router-dom';

import axios, { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import QUERY_KEYS from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useCreatePost = (
  options?: UseMutationOptions<
    AxiosResponse<string, string>,
    AxiosError,
    Pick<Post, 'title' | 'content'> & { hashtags: string[]; anonymous?: boolean }
  >,
) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useSnackbar();
  const { boardId } = useLocation().state as Pick<Post, 'boardId'>;

  return useMutation(
    ({
      title,
      content,
      hashtags,
      anonymous,
    }: Pick<Post, 'title' | 'content'> & { hashtags: string[]; anonymous?: boolean }): Promise<
      AxiosResponse<string, string>
    > =>
      axios.post(`/boards/${boardId}/posts`, {
        title,
        content,
        hashtags,
        anonymous,
      }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.resetQueries(QUERY_KEYS.POSTS);
        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_WRITE_POST);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
    },
  );
};

export default useCreatePost;
