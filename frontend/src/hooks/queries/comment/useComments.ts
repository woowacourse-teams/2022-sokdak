import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS from '@/constants/queries';

interface CommentResponse extends CommentType {
  id: number;
  blocked: boolean;
  postWriter: boolean;
  replies: Omit<CommentResponse, 'replies'>[];
}

const useComments = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<
    AxiosResponse<Record<'comments', CommentResponse[]>>,
    AxiosError,
    CommentResponse[],
    QueryKey[]
  >;
}) =>
  useQuery([QUERY_KEYS.COMMENTS, storeCode], ({ queryKey: [, id] }) => authFetcher.get(`/posts/${id}/comments`), {
    select: data => data.data.comments,
    ...options,
  });

export default useComments;
