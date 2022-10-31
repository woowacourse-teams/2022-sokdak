import { useContext } from 'react';
import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

import { createReportComment } from '@/api/comment';
import type { CreateReportCommentRequest } from '@/api/comment';
import { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface UsePostReportProps extends CreateReportCommentRequest {
  id: number;
}

const useReportComment = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<{ message: string }>, UsePostReportProps>,
) => {
  const { showSnackbar } = useContext(SnackbarContext);

  return useMutation(({ id, message }) => createReportComment(String(id), { message }), {
    onSuccess: () => {
      showSnackbar(SNACKBAR_MESSAGE.SUCCESS_REPORT_COMMENT);
    },
    mutationKey: MUTATION_KEY.REPORT_COMMENT,
    ...options,
  });
};

export default useReportComment;
