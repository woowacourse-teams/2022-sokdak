import { useQuery, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

interface PostListByBoards {
  boards: {
    id: number;
    title: string;
    posts: Pick<Post, 'id' | 'likeCount' | 'commentCount' | 'title'>[];
  }[];
}

const usePostByBoards = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<AxiosResponse<PostListByBoards>, AxiosError<{ message: string }>, PostListByBoards, string>,
    'queryKey' | 'queryFn'
  >;
}) =>
  useQuery(QUERY_KEYS.POSTS_BY_BOARDS, () => axios.get('/boards/contents'), {
    select: data => data.data,
    ...options,
  });

export default usePostByBoards;
