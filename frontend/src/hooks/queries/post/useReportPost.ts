import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createPostReport } from '@/api/post';
import { MUTATION_KEY } from '@/constants/queries';

interface PostReportProps {
  id: number;
  message: string;
}

const useReportPost = (
  options?: UseMutationOptions<AxiosResponse<null>, AxiosError<{ message: string }>, PostReportProps>,
) =>
  useMutation(({ id, message }) => createPostReport(id!, { message }), {
    ...options,
    mutationKey: MUTATION_KEY.REPORT_POST,
  });
export default useReportPost;
