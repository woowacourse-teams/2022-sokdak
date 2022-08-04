import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';

interface PostReportProps {
  id: number;
  message: string;
}

const useReportPost = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, PostReportProps>,
) => {
  return useMutation(
    ({ id, message }) => {
      return authFetcher.post(`/posts/${id}/report`, { message });
    },
    {
      ...options,
    },
  );
};

export default useReportPost;
