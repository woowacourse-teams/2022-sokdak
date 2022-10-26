import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { requestGetPost } from '@/apis/post';
import QUERY_KEYS from '@/constants/queries';

const usePost = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<AxiosResponse<Post & { boardId: string }>, AxiosError, Post, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.POST, storeCode], ({ queryKey: [, id] }) => requestGetPost(String(id)), {
    staleTime: 1000 * 20,
    ...options,
  });

export default usePost;
