import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetComment } from '@/apis/comment';
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
  options?: UseQueryOptions<CommentResponse, AxiosError, CommentResponse, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.COMMENTS, storeCode], ({ queryKey: [, id] }) => requestGetComment(String(id)), {
    ...options,
  });

export default useComments;
