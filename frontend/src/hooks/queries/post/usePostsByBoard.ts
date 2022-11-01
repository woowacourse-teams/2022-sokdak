import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetPostsByBoards } from '@/api/post';
import type { GetPostsByBoardsResponse } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

const usePostByBoards = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<GetPostsByBoardsResponse['boards'], AxiosError<Error>, GetPostsByBoardsResponse['boards'], string>,
    'queryKey' | 'queryFn'
  >;
}) =>
  useQuery(QUERY_KEYS.POSTS_BY_BOARDS, () => requestGetPostsByBoards(), {
    staleTime: 1000 * 20,
    ...options,
  });

export default usePostByBoards;
