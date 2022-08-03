import { useQuery, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

interface BoardsResponse {
  boards: Board[];
}

const useBoards = ({
  options,
}: {
  options?: UseQueryOptions<AxiosResponse<BoardsResponse>, AxiosError, Board[], string>;
}) =>
  useQuery(QUERY_KEYS.BOARDS, () => axios.get('/boards'), {
    select: data => data.data.boards,
    ...options,
  });

export default useBoards;
