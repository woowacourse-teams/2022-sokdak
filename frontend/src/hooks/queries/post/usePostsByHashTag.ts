import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetPostsByHashtags } from '@/api/post';
import type { GetPostsByHashtagsResponse } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

type HashtagName = string;
type Size = number;

const usePostsByHashTag = ({
  storeCode,
  options,
}: {
  storeCode: [HashtagName, Size];
  options?: UseInfiniteQueryOptions<
    GetPostsByHashtagsResponse,
    AxiosError,
    Post,
    GetPostsByHashtagsResponse,
    [QueryKey, HashtagName, Size]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, hashtagName, size] }) => requestGetPostsByHashtags(hashtagName, size, pageParam),
    {
      select: data => ({
        pages: data.pages.flatMap((page: GetPostsByHashtagsResponse) => page.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.lastPage ? undefined : allPages.length),
      retry: false,
      ...options,
    },
  );

export default usePostsByHashTag;
