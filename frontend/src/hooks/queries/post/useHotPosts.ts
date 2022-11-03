import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { GetPostsResponse, requestGetPosts } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

const useHotPosts = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<GetPostsResponse, AxiosError, GetPostsResponse, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.POST, storeCode], ({ queryKey: [, size] }) => requestGetPosts('1', Number(size), '0'), {
    staleTime: 1000 * 40,
    ...options,
  });

export default useHotPosts;
