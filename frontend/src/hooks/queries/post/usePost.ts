import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetPost } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

const usePost = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<Promise<Post>, AxiosError, Post, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.POST, storeCode], ({ queryKey: [, id] }) => requestGetPost(String(id)), {
    staleTime: 1000 * 20,
    ...options,
  });

export default usePost;
