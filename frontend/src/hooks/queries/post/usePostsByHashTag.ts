import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import axios, { AxiosResponse, AxiosError } from 'axios';

import QUERY_KEYS from '@/constants/queries';

type HashtagName = string;
type Size = number;

const usePostsByHashTag = ({
  storeCode,
  options,
}: {
  storeCode: [HashtagName, Size];
  options?: UseInfiniteQueryOptions<
    AxiosResponse<{ posts: Post; lastPage: boolean }>,
    AxiosError,
    Post,
    AxiosResponse<{ posts: Post; lastPage: boolean }>,
    [QueryKey, HashtagName, Size]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, hashtagName, size] }) =>
      axios.get(`/posts?hashtag=${hashtagName}&size=${size}&page=${pageParam}`),
    {
      select: data => ({
        pages: data.pages.flatMap((page: AxiosResponse) => page.data.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.data.lastPage ? undefined : allPages.length),
      ...options,
    },
  );

export default usePostsByHashTag;
