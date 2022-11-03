import fetcher from './fetcher';

export interface GetBoardsResponse {
  boards: Board[];
}

export const requestGetBoards = async () => {
  const { data } = await fetcher.get<GetBoardsResponse>('/boards');

  return data.boards;
};
