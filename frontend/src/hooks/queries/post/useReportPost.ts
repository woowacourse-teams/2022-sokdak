import { useMutation, UseMutationOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

interface PostReportProps {
  id: number;
  message: string;
}

const useReportPost = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, PostReportProps>,
) => {
  return useMutation(
    ({ id, message }) => {
      return axios.post(`/posts/${id}/report`, { message });
    },
    {
      ...options,
    },
  );
};

export default useReportPost;
