import authFetcher from './authFetcher';

export const requestGetComment = async (id: string) => {
  const { data } = await authFetcher.get(`/posts/${id}/comments`);

  return data;
};

export const createComment = (id: string, body: { content: string; anonymous: boolean }) =>
  authFetcher.post(`posts/${id}/comments`, body);

export const requestDeleteComment = (id: string) => authFetcher.delete(`/comments/${id}`);

export const requestPutLikeComment = async (id: string) => {
  const { data } = await authFetcher.put(`/comments/${id}/like`);

  return data;
};

export const createReportComment = (id: string, body: { message: string }) =>
  authFetcher.post(`/comments/${id}/report`, body);

export const createReply = (id: string, body: { content: string; anonymous: boolean }) =>
  authFetcher.post(`comments/${id}/reply`, body);
