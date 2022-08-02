import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

interface CommentResponse extends CommentType {
  id: string;
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
  useQuery([QUERY_KEYS.COMMENTS, storeCode], ({ queryKey: [, id] }) => axios.get(`/posts/${id}/comments`), {
    select: data => data.data.comments,
    ...options,
  });

export default useComments;
