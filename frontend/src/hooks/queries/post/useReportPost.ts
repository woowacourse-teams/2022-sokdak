import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import { MUTATION_KEY } from '@/constants/queries';

interface PostReportProps {
  id: number;
  message: string;
  boardId: number;
}

const useReportPost = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, PostReportProps>,
) => {
  return useMutation(
    ({ id, message, boardId }) => {
      return authFetcher.post(`/posts/${id}/report`, { message, boardId });
    },
    {
      ...options,
      mutationKey: MUTATION_KEY.REPORT_POST,
    },
  );
};

export default useReportPost;
