import api from '.';
import authFetcher from './authFetcher';

export const requestGetPost = async (id: string) => {
  const { data } = await authFetcher.get(`/posts/${id}`);

  return data;
};

export const requestGetPosts = async (
  boardId: string,
  size: number,
  pageParam: string,
): Promise<{ posts: Post[]; lastPage: boolean }> => {
  const { data } = await api.get(`/boards/${boardId}/posts?size=${size}&page=${pageParam}`);

  return data;
};

export const createNewPost = (
  boardId: string | number,
  body: Pick<Post, 'title' | 'content' | 'imageName'> & {
    anonymous?: boolean;
    hashtags: string[];
  },
) => authFetcher.post(`/boards/${boardId}/posts`, body);

export const requestDeletePost = (id: string) => authFetcher.delete(`/posts/${id}`);

export const requestGetPostsByBoards = async () => {
  const { data } = await api.get('/boards/contents');

  return data;
};

export const requestGetPostsByHashtags = async (
  hashtagName: string,
  size: number,
  pageParam: string,
): Promise<{ posts: Post[]; lastPage: boolean }> => {
  const { data } = await api.get(`/posts?hashtag=${hashtagName}&size=${size}&page=${pageParam}`);

  return data;
};

export const createPostReport = (id: number, body: { message: string }) =>
  authFetcher.post(`/posts/${id}/report`, body);

export const requestGetSearchPostCount = async (query: string) => {
  const { data } = await api.get(
    `/posts/count?query=${query.replaceAll(' ', '%7C').replaceAll('|', '%7C').replaceAll('+', '%7C')}`,
  );

  return data;
};

export const requestGetSearchPosts = async (query: string, size: number, pageParam: number) => {
  const { data } = await api.get(
    `/posts?query=${query
      .replaceAll(' ', '%7C')
      .replaceAll('|', '%7C')
      .replaceAll('+', '%7C')}&size=${size}&page=${pageParam}`,
  );
  return data;
};

export const requestUpdatePost = (
  id: string,
  body: Pick<Post, 'title' | 'content' | 'imageName'> & { hashtags: string[] },
) => authFetcher.put(`/posts/${id}`, body);

export const createImage = (image: FormData) => authFetcher.post(process.env.IMAGE_API_URL!, image);

export const requestUpdateLikePost = (id: string) => authFetcher.put(`/posts/${id}/like`);
