import { useContext } from 'react';
import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

import QUERY_KEYS from '@/constants/queries';

interface DeleteCommentProps {
  id: number;
}

const useDeleteComment = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, DeleteCommentProps>,
) => {
  const { showSnackbar } = useContext(SnackbarContext);
  const queryClient = useQueryClient();

  return useMutation(
    ({ id }) => {
      return axios.delete(`/comments/${id}`);
    },
    {
      onSuccess: () => {
        queryClient.resetQueries(QUERY_KEYS.COMMENTS);
        showSnackbar('성공적으로 댓글이 삭제되었습니다.');
      },
      onError: err => {
        showSnackbar(err.response?.data.message!);
      },
      ...options,
    },
  );
};

export default useDeleteComment;
