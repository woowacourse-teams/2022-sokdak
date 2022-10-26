import { useContext } from 'react';
import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

import { createReportComment } from '@/apis/comment';
import { MUTATION_KEY } from '@/constants/queries';
import SNACKBAR_MESSAGE from '@/constants/snackbar';

interface PostReportProps {
  id: number;
  message: string;
}

const useReportComment = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<{ message: string }>, PostReportProps>,
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
