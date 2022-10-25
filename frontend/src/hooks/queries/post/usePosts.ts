import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosResponse, AxiosError } from 'axios';

import api from '@/apis';
import QUERY_KEYS from '@/constants/queries';

type BoardId = string | number;
type Size = number;

const usePosts = ({
  storeCode,
  options,
}: {
  storeCode: [BoardId, Size];
  options?: UseInfiniteQueryOptions<
    AxiosResponse<{ posts: Post; lastPage: boolean }>,
    AxiosError,
    Post,
    AxiosResponse<{ posts: Post; lastPage: boolean }>,
    [QueryKey, BoardId, Size]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, boardId, size] }) =>
      api.get(`/boards/${boardId}/posts?size=${size}&page=${pageParam}`),
    {
      select: data => ({
        pages: data.pages.flatMap((page: AxiosResponse) => page.data.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      enabled: false,
      getNextPageParam: (lastPage, allPages) => (lastPage.data.lastPage ? undefined : allPages.length),
      ...options,
    },
  );

export default usePosts;
