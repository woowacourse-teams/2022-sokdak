import { useQueryClient, useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useUpdatePost = ({
  id,
  options,
}: {
  id: string | number;
  options?: UseMutationOptions<AxiosResponse, AxiosError, Pick<Post, 'title' | 'content'> & { hashtags: string[] }>;
}) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useSnackbar();

  return useMutation(
    ({ title, content, hashtags }: Pick<Post, 'title' | 'content'> & { hashtags: string[] }): Promise<AxiosResponse> =>
      authFetcher.put(`/posts/${id}`, {
        title,
        content,
        hashtags,
      }),
    {
      ...options,
      onSuccess: (data, variables, context) => {
        queryClient.invalidateQueries(QUERY_KEYS.POSTS);
        queryClient.invalidateQueries(QUERY_KEYS.POSTS_BY_BOARDS);
        queryClient.invalidateQueries(QUERY_KEYS.MY_POSTS);
        queryClient.invalidateQueries([QUERY_KEYS.POST, String(id)]);

        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_UPDATE_POST);

        if (options && options.onSuccess) {
          options.onSuccess(data, variables, context);
        }
      },
      mutationKey: MUTATION_KEY.UPDATE_POST,
    },
  );
};

export default useUpdatePost;
