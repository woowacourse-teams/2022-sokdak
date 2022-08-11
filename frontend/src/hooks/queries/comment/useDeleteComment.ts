import { useContext } from 'react';
import { useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

import authFetcher from '@/apis';
import QUERY_KEYS, { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

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
      return authFetcher.delete(`/comments/${id}`);
    },
    {
      onSuccess: () => {
        queryClient.refetchQueries(QUERY_KEYS.COMMENTS);
        showSnackbar(SNACKBAR_MESSAGE.SUCCESS_DELETE_COMMENT);
      },
      mutationKey: MUTATION_KEY.DELETE_COMMENT,
      ...options,
    },
  );
};

export default useDeleteComment;
