import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS from '@/constants/queries';

const usePost = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<AxiosResponse<Post & { boardId: string }>, AxiosError, Post, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.POST, storeCode], ({ queryKey: [, id] }) => authFetcher.get(`/posts/${id}`), {
    select: data => data.data,
    suspense: true,
    ...options,
  });

export default usePost;
