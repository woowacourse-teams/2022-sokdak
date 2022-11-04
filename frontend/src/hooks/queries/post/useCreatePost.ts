import { useQueryClient, useMutation, UseMutationOptions } from 'react-query';

import { AxiosError } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import { createNewPost } from '@/api/post';
import type { CreateNewPostRequest } from '@/api/post';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface UseCreatePostProps extends CreateNewPostRequest {
  boardId: number;
}

const useCreatePost = (options?: UseMutationOptions<null, AxiosError, CreateNewPostRequest>) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useSnackbar();

  return useMutation(
    ({ title, content, hashtags, anonymous, boardId, imageName }: UseCreatePostProps) =>
      createNewPost(boardId, {
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
