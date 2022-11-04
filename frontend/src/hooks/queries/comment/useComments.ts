import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetComment } from '@/api/comment';
import type { GetCommentResponse } from '@/api/comment';
import QUERY_KEYS from '@/constants/queries';

const useComments = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<GetCommentResponse, AxiosError, GetCommentResponse, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.COMMENTS, storeCode], ({ queryKey: [, id] }) => requestGetComment(String(id)), {
    ...options,
  });

export default useComments;
