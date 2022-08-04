import { useContext } from 'react';
import { useQueryClient, useMutation, UseMutationOptions } from 'react-query';
import { useNavigate } from 'react-router-dom';

import { AxiosError, AxiosResponse } from 'axios';

import AuthContext from '@/context/Auth';

import useSnackbar from '@/hooks/useSnackbar';

import authFetcher from '@/apis';
import PATH from '@/constants/path';
import QUERY_KEYS from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

const useCreatePost = (
  options?: UseMutationOptions<
    AxiosResponse<string, string>,
    AxiosError,
    Pick<Post, 'title' | 'content'> & { hashtags: string[]; anonymous?: boolean; boardId?: string | number }
  >,
) => {
  const queryClient = useQueryClient();
  const { showSnackbar } = useSnackbar();
  const { setIsLogin, setUserName } = useContext(AuthContext);
  const navigate = useNavigate();

  return useMutation(
    ({
      title,
      content,
      hashtags,
      anonymous,
      boardId,
    }: Pick<Post, 'title' | 'content'> & {
      hashtags: string[];
      anonymous?: boolean;
      boardId?: string | number;
    }): Promise<AxiosResponse<string, string>> =>
      authFetcher.post(`/boards/${boardId}/posts`, {
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
      onError: (error, variables, context) => {
        if (error.response?.status === 401) {
          showSnackbar('로그인을 해주세요');
          setIsLogin(false);
          setUserName('');
          navigate(PATH.LOGIN);
        }
        if (options && options.onError) {
          options.onError(error, variables, context);
        }
      },
    },
  );
};

export default useCreatePost;
