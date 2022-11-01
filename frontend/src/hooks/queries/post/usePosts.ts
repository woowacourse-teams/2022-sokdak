import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetPosts } from '@/api/post';
import type { GetPostsResponse } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

type BoardId = string | number;
type Size = number;

const usePosts = ({
  storeCode,
  options,
}: {
  storeCode: [BoardId, Size];
  options?: UseInfiniteQueryOptions<GetPostsResponse, AxiosError, Post, GetPostsResponse, [QueryKey, BoardId, Size]>;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, boardId, size] }) => requestGetPosts(String(boardId), size, pageParam),
    {
      select: data => ({
        pages: data.pages.flatMap((page: GetPostsResponse) => page.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      enabled: false,
      getNextPageParam: (lastPage, allPages) => (lastPage.lastPage ? undefined : allPages.length),
      staleTime: 1000 * 20,
      ...options,
    },
  );

export default usePosts;
