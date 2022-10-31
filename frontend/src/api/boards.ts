import api from '.';

export interface GetBoardsResponse {
  boards: Board[];
}

export const requestGetBoards = async () => {
  const { data } = await api.get<GetBoardsResponse>('/boards');

  return data.boards;
};
