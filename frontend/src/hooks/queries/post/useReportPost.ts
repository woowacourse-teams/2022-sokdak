import { useMutation, UseMutationOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { createPostReport, CreatePostReportRequest } from '@/api/post';
import { MUTATION_KEY } from '@/constants/queries';

interface UseReportPostProps extends CreatePostReportRequest {
  id: number;
}

const useReportPost = (options?: UseMutationOptions<AxiosResponse<null>, AxiosError<Error>, UseReportPostProps>) =>
  useMutation(({ id, message }) => createPostReport(id, { message }), {
    ...options,
    mutationKey: MUTATION_KEY.REPORT_POST,
  });
export default useReportPost;
