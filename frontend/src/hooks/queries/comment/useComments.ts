import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS from '@/constants/queries';

interface CommentList extends CommentType {
  id: number;
  blocked: boolean;
  postWriter: boolean;
  replies: Omit<CommentList, 'replies'>[];
}

interface CommentResponse {
  comments: CommentList[];
  totalCount: number;
}

const useComments = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<AxiosResponse<CommentResponse>, AxiosError, CommentResponse, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.COMMENTS, storeCode], ({ queryKey: [, id] }) => authFetcher.get(`/posts/${id}/comments`), {
    select: data => data.data,
    suspense: true,
    ...options,
  });

export default useComments;
