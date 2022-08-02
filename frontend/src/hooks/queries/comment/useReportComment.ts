import { useContext } from 'react';
import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import SnackbarContext from '@/context/Snackbar';

interface PostReportProps {
  id: number;
  message: string;
}

const useReportComment = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, PostReportProps>,
) => {
  const { showSnackbar } = useContext(SnackbarContext);

  return useMutation(
    ({ id, message }) => {
      return axios.post(`/comments/${id}/report`, { message });
    },
    {
      onSuccess: () => {
        showSnackbar('신고에 성공하였습니다.');
      },
      onError: err => {
        showSnackbar(err.response?.data.message!);
      },
      ...options,
    },
  );
};

export default useReportComment;
