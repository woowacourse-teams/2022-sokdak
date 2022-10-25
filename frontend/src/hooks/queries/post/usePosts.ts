import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetPosts } from '@/apis/post';
import QUERY_KEYS from '@/constants/queries';

type BoardId = string | number;
type Size = number;

const usePosts = ({
  storeCode,
  options,
}: {
  storeCode: [BoardId, Size];
  options?: UseInfiniteQueryOptions<
    { posts: Post[]; lastPage: boolean },
    AxiosError,
    Post,
    { posts: Post[]; lastPage: boolean },
    [QueryKey, BoardId, Size]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, boardId, size] }) => requestGetPosts(String(boardId), size, pageParam),
    {
      select: data => ({
        pages: data.pages.flatMap((page: { posts: Post[]; lastPage: boolean }) => page.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      enabled: false,
      getNextPageParam: (lastPage, allPages) => (lastPage.lastPage ? undefined : allPages.length),
      ...options,
    },
  );

export default usePosts;
