import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError } from 'axios';

import { createPostReport, CreatePostReportRequest } from '@/api/post';
import { MUTATION_KEY } from '@/constants/queries';

interface UseReportPostProps extends CreatePostReportRequest {
  id: number;
}

const useReportPost = (options?: UseMutationOptions<null, AxiosError, UseReportPostProps>) =>
  useMutation(({ id, message }) => createPostReport(id, { message }), {
    ...options,
    mutationKey: MUTATION_KEY.REPORT_POST,
  });
export default useReportPost;
