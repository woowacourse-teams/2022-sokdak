import { AxiosResponse } from 'axios';

import fetcher from './fetcher';
import authFetcher from './fetcher/auth';

export const requestGetPost = async (id: string) => {
  const { data } = await authFetcher.get<Post>(`/posts/${id}`);

  return data;
};

export interface GetPostsResponse {
  posts: Post[];
  lastPage: boolean;
}

export const requestGetPosts = async (boardId: string, size: number, pageParam: string) => {
  const { data } = await fetcher.get<GetPostsResponse>(`/boards/${boardId}/posts?size=${size}&page=${pageParam}`);

  return data;
};

export interface CreateNewPostRequest extends Pick<Post, 'title' | 'content' | 'imageName'> {
  anonymous?: boolean;
  hashtags: string[];
}

export const createNewPost = (boardId: string | number, body: CreateNewPostRequest) =>
  authFetcher.post<null, AxiosResponse<null>, CreateNewPostRequest>(`/boards/${boardId}/posts`, body);

export const requestDeletePost = (id: string) => authFetcher.delete(`/posts/${id}`);

export interface GetPostsByBoardsResponse {
  boards: {
    id: number;
    title: string;
    posts: Pick<Post, 'id' | 'likeCount' | 'commentCount' | 'title'>[];
  }[];
}

export const requestGetPostsByBoards = async () => {
  const { data } = await fetcher.get<GetPostsByBoardsResponse>('/boards/contents');

  return data.boards;
};

export interface GetPostsByHashtagsResponse {
  posts: Post[];
  lastPage: boolean;
}

export const requestGetPostsByHashtags = async (hashtagName: string, size: number, pageParam: string) => {
  const { data } = await fetcher.get<GetPostsByHashtagsResponse>(
    `/posts?hashtag=${hashtagName}&size=${size}&page=${pageParam}`,
  );

  return data;
};

export interface CreatePostReportRequest {
  message: string;
}

export const createPostReport = (id: number, body: CreatePostReportRequest) =>
  authFetcher.post<null, AxiosResponse<null>, CreatePostReportRequest>(`/posts/${id}/report`, body);

export interface GetSearchPostCountResponse {
  totalPostCount: number;
}

export const requestGetSearchPostCount = async (query: string) => {
  const { data } = await fetcher.get<GetSearchPostCountResponse>(
    `/posts/count?query=${query.replaceAll(' ', '%7C').replaceAll('|', '%7C').replaceAll('+', '%7C')}`,
  );

  return data.totalPostCount;
};

export interface GetSearchPostsResponse {
  posts: Post[];
  lastPage: boolean;
}

export const requestGetSearchPosts = async (query: string, size: number, pageParam: number) => {
  const { data } = await fetcher.get<GetSearchPostsResponse>(
    `/posts?query=${query
      .replaceAll(' ', '%7C')
      .replaceAll('|', '%7C')
      .replaceAll('+', '%7C')}&size=${size}&page=${pageParam}`,
  );
  return data;
};

export interface UpdatePostRequest extends Pick<Post, 'title' | 'content' | 'imageName'> {
  hashtags: string[];
}

export const requestUpdatePost = (id: string, body: UpdatePostRequest) =>
  authFetcher.put<null, AxiosResponse<null>, UpdatePostRequest>(`/posts/${id}`, body);

export interface CreateImageResponse {
  imageName: string;
}

export const createImage = (image: FormData) =>
  authFetcher.post<CreateImageResponse, AxiosResponse<CreateImageResponse>, FormData>(
    process.env.IMAGE_API_URL!,
    image,
  );

export interface UpdateLikePostResponse {
  like: boolean;
  likeCount: number;
}

export const requestUpdateLikePost = (id: string) => authFetcher.put<UpdateLikePostResponse>(`/posts/${id}/like`);

export interface GetMyPostResponse {
  posts: Post[];
  totalPageCount: number;
}

export const requestGetMyPost = async (size: number, page: number) => {
  const { data } = await authFetcher.get<GetMyPostResponse>(`/posts/me?size=${size}&page=${page - 1}`);

  return data;
};
