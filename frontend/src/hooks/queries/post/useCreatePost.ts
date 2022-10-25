import { useQueryClient, useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis/authFetcher';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useCreatePost = (
  options?: UseMutationOptions<
    AxiosResponse<string, string>,
    AxiosError,
    Pick<Post, 'title' | 'content' | 'imageName'> & {
      hashtags: string[];
      anonymous?: boolean;
      boardId?: number;
    }
  >,
) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useSnackbar();

  return useMutation(
    ({
      title,
      content,
      hashtags,
      anonymous,
      boardId,
      imageName,
    }: Pick<Post, 'title' | 'content' | 'imageName'> & {
      hashtags: string[];
      anonymous?: boolean;
      boardId?: string | number;
    }): Promise<AxiosResponse<string, string>> =>
      authFetcher.post(`/boards/${boardId}/posts`, {
        title,
        content,
        hashtags,
        anonymous,
        imageName,
      }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.invalidateQueries(QUERY_KEYS.POSTS);
        queryClient.invalidateQueries(QUERY_KEYS.POSTS_BY_BOARDS);
        queryClient.invalidateQueries(QUERY_KEYS.MY_POSTS);

        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_WRITE_POST);
        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
      mutationKey: MUTATION_KEY.CREATE_POST,
    },
  );
};

export default useCreatePost;
