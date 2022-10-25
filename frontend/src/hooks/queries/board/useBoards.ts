import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import api from '@/apis';
import QUERY_KEYS from '@/constants/queries';

interface BoardsResponse {
  boards: Board[];
}

const useBoards = ({
  options,
}: {
  options?: UseQueryOptions<AxiosResponse<BoardsResponse>, AxiosError, Board[], string>;
}) =>
  useQuery(QUERY_KEYS.BOARDS, () => api.get('/boards'), {
    select: data => data.data.boards,
    ...options,
  });

export default useBoards;
